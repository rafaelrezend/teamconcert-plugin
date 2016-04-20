/*******************************************************************************
 * Copyright (c) 2014, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.team.build.internal.hjplugin.util;

import java.util.Locale;
import java.util.Map;
import com.ibm.team.build.internal.hjplugin.RTCBuildResultAction;
import com.ibm.team.build.internal.hjplugin.RTCFacadeFactory;
import com.ibm.team.build.internal.hjplugin.RTCLoginInfo;
import com.ibm.team.build.internal.hjplugin.RTCFacadeFactory.RTCFacadeWrapper;

import hudson.Util;
import hudson.model.Job;
import hudson.model.ParameterValue;
import hudson.model.TaskListener;
import hudson.model.ParametersAction;
import hudson.model.Run;
import hudson.model.StringParameterValue;
import hudson.util.FormValidation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;

import com.ibm.team.build.internal.hjplugin.Messages;

public class Helper {
	private static final Logger LOGGER = Logger.getLogger(Helper.class.getName());
	
	public static final String COMPONENTS_TO_EXCLUDE = "componentsToExclude"; //$NON-NLS-1$
	public static final String LOAD_RULES = "loadRules"; //$NON-NLS-1$
	public static final String COMPONENT_ID = "componentId"; //$NON-NLS-1$
	public static final String COMPONENT_NAME = "componentName"; //$NON-NLS-1$
	public static final String FILE_ITEM_ID = "fileItemId"; //$NON-NLS-1$
	public static final String FILE_PATH = "filePath"; //$NON-NLS-1$
	

	/** 
	 * merge two results, if both are errors only one stack trace can be included
	 * @param firstCheck The first validation done
	 * @param secondCheck The second validaiton done
	 * @return The merge of the 2 validations with a concatenated message and the highest severity
	 */
	public static FormValidation mergeValidationResults(
			FormValidation firstCheck, FormValidation secondCheck) {
		Throwable errorCause = secondCheck.getCause();
		if (errorCause == null) {
			errorCause = firstCheck.getCause();
		}
		String message;
		String firstMessage = firstCheck.getMessage();
		String secondMessage = secondCheck.getMessage();
		if (firstCheck.kind.equals(FormValidation.Kind.OK) && (firstMessage == null || firstMessage.isEmpty())) {
			message = secondCheck.renderHtml();
		} else if (secondCheck.kind.equals(FormValidation.Kind.OK) && (secondMessage == null || secondMessage.isEmpty())) {
			message = firstCheck.renderHtml();
		} else {
			message = firstCheck.renderHtml() +  "<br/>" + secondCheck.renderHtml(); //$NON-NLS-1$
		}
		FormValidation.Kind kind;
		if (firstCheck.kind.equals(secondCheck.kind)) {
			kind = firstCheck.kind;
		} else if (firstCheck.kind.equals(FormValidation.Kind.OK)) {
			kind = secondCheck.kind;
		} else if (firstCheck.kind.equals(FormValidation.Kind.ERROR) || secondCheck.kind.equals(FormValidation.Kind.ERROR)) {
			kind = FormValidation.Kind.ERROR;
		} else {
			kind = FormValidation.Kind.WARNING;
		}
		
		return FormValidation.respond(kind, message);
	}
	
	public static String getStringBuildProperty(Run<?,?> build, String property, TaskListener listener) throws IOException, InterruptedException {
		 LOGGER.finest("Helper.getStringBuildProperty : Begin"); //$NON-NLS-1$
		 if (LOGGER.isLoggable(Level.FINEST)) {
			 LOGGER.finest("Helper.getStringBuildProperty: Finding value for property '" + property + "' in the build environment variables.");	  //$NON-NLS-1$ //$NON-NLS-2$
		 }
		 String value = Util.fixEmptyAndTrim(build.getEnvironment(listener).get(property));
		 if (value == null) {
			if (LOGGER.isLoggable(Level.FINEST)) {
				LOGGER.finest("Helper.getStringBuildProperty: Cannot find value for property '" + property + "' in the build environment variables. Looking in the build parameters."); //$NON-NLS-1$ //$NON-NLS-2$
			}
			// check if parameter is available from ParametersAction
			value = getValueFromParametersAction(build, property);			
			if (value == null) {
				if (LOGGER.isLoggable(Level.FINEST)) {
					LOGGER.finest("Helper.getStringBuildProperty: Cannot find value for property '" + property + "' in the build parameters."); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		 }
		if (LOGGER.isLoggable(Level.FINEST)) {
			LOGGER.finest("Helper.getStringBuildProperty: Value for property '" + property + "' is '" + value + "'."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		 return value;
	}
	
	/**
	 * Reads the json text, specifying the components to exclude during load, from the given path on disk and validates
	 * that required fields are provided.
	 * 
	 * @param componentsToExcludefilePath path to a file on disk
	 * @return json text specifying the components to exclude during load
	 * @throws Exception
	 */
	public static String validateAndGetComponentsToExcludeJson(String componentsToExcludefilePath) throws Exception {
		String componentsToExcludeJsonStr = readJsonFromFile(componentsToExcludefilePath);
		if (componentsToExcludeJsonStr != null) {
			LOGGER.finer("Helper.validateAndGetComponentsToExcludeJson: stepping through and validating components to exclude json"); //$NON-NLS-1$
			JSONObject json = JSONObject.fromObject(componentsToExcludeJsonStr);
			if (!json.has(COMPONENTS_TO_EXCLUDE)) {
				throw new IllegalArgumentException(Messages.Helper_components_to_exclude_required());
			}
			JSONArray componentsArray = json.getJSONArray(COMPONENTS_TO_EXCLUDE);
			if (componentsArray.isEmpty()) {
				throw new IllegalArgumentException(Messages.Helper_components_to_exclude_required());
			}
			for (int i = 0; i < componentsArray.size(); i++) {
				JSONObject inner = (JSONObject)componentsArray.get(i);
				if (!inner.has(COMPONENT_ID) && !inner.has(COMPONENT_NAME)) {
					throw new IllegalArgumentException(Messages.Helper_component_id_or_name_required());
				}
				if (!inner.has(COMPONENT_ID) && inner.has(COMPONENT_NAME) && inner.getString(COMPONENT_NAME).trim().length() == 0) {
					throw new IllegalArgumentException(Messages.Helper_component_name_empty_components_to_exclude());
				}
			}
		}
		return componentsToExcludeJsonStr;
	}

	/**
	 * Reads the json text, specifying the component-to-load-rule-mapping to be enforced during load, from the given
	 * path on disk and validates that the required fields are provided.
	 * 
	 * 
	 * @param loadRulesFilePath path to a file on disk
	 * @return json text specifying the component-to-load-rule-mapping to be enforced during load
	 * @throws Exception
	 */
	public static String validateAndGetLoadRulesJson(String loadRulesFilePath) throws Exception {
		String loadRulesJsonStr = readJsonFromFile(loadRulesFilePath);
		if (loadRulesJsonStr != null) {
			LOGGER.finer("Helper.validateAndGetLoadRulesJson: stepping through and validating load rules json"); //$NON-NLS-1$
			JSONObject json = JSONObject.fromObject(loadRulesJsonStr);
			if (!json.has(LOAD_RULES)) {
				throw new IllegalArgumentException(Messages.Helper_load_rules_required());
			}
			JSONArray loadRulesArray = json.getJSONArray(LOAD_RULES);
			if (loadRulesArray.isEmpty()) {
				throw new IllegalArgumentException(Messages.Helper_load_rules_required());
			}
			for (int i = 0; i < loadRulesArray.size(); i++) {
				JSONObject inner = (JSONObject)loadRulesArray.get(i);
				if (!inner.has(COMPONENT_ID) && !inner.has(COMPONENT_NAME)) {
					throw new IllegalArgumentException(Messages.Helper_component_id_or_name_required());
				}
				if (!inner.has(COMPONENT_ID) && inner.has(COMPONENT_NAME) && inner.getString(COMPONENT_NAME).trim().length() == 0) {
					throw new IllegalArgumentException(Messages.Helper_component_name_empty_load_rules());
				}
				if (!inner.has(FILE_ITEM_ID) && !inner.has(FILE_PATH)) {
					throw new IllegalArgumentException(Messages.Helper_file_item_id_or_name_required());
				}
			}
		}
		return loadRulesJsonStr;
	}

	private static String getValueFromParametersAction(Run<?, ?> build, String key) {
		String value = null;
		LOGGER.finest("Helper.getValueFromParametersAction : Begin"); //$NON-NLS-1$
		for (ParametersAction paction : build.getActions(ParametersAction.class)) {
			List<ParameterValue> pValues = paction.getParameters();
			if (pValues == null) {
				continue;
			}
			for (ParameterValue pv : pValues) {
				if (pv instanceof StringParameterValue && pv.getName().equals(key)) {
					value = Util.fixEmptyAndTrim((String)pv.getValue());
					if (value != null) {
						break;
					}
				}
			}
			if (value != null) {
				break;
			}
		}
		if (LOGGER.isLoggable(Level.FINEST)) {
			if (value == null) {
				LOGGER.finest("Helper.getValueFromParametersAction : Unable to find a value for key : " + key); //$NON-NLS-1$
			} else {
				LOGGER.finest("Helper.getValueFromParametersAction : Found value : " + value + " for key : " + key);  //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		return value;
	}
	
	/**
	 * Checks whether a given string is a property like ${a} 
	 * @param s The string to check. May be <code>null</code>
	 * @return <code>true</code> if s is a job property 
	 */
	public static boolean isJobProperty(String s) {
		if (s == null) {
			return false;
		}
		if (s.startsWith("${") && s.endsWith("}")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Resolve a given job property to its value
	 * @param build - the Jenkins build. Never <code>null</code>
	 * @param property - the job property. Never <code>null</code>
	 * @param listener - task listener. Never <code>null</code>
	 * @return the value of the job property or <code>property</code> if the job property is not defined or 
	 * if the given property is not a job property 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static String resolveJobProperty(Run<?,?> build, String property, TaskListener listener) throws IOException, InterruptedException {
		String propertyStripped = property.substring(2);
		propertyStripped = propertyStripped.substring(0, propertyStripped.length() - 1);
		return getStringBuildProperty(build, propertyStripped, listener);
	}
	
	/**
	 * Returns the snapshot UUID from the previous build
	 * @param build
	 * @param facade
	 * @param loginInfo
	 * @param streamName
	 * @param clientLocale
	 * @return
	 * @throws Exception
	 */
	public static String getSnapshotUUIDFromPreviousBuild(final Run<?,?> build, String toolkit, RTCLoginInfo loginInfo, String buildStream, final boolean onlyGoodBuild, Locale clientLocale) throws Exception {
		if (buildStream == null) {
			return null;
		}
		String snapshotUUID = getValueForBuildStream(new IJenkinsBuildIterator() {
			
			@Override
			public Run<?,?> nextBuild(Run<?, ?> build) {
				if (onlyGoodBuild) {
					return build.getPreviousSuccessfulBuild();
				} else {
					return build.getPreviousBuild();
				}
			}
			
			@Override
			public Run<?, ?> firstBuild() {
				return build;
			}
		}, toolkit, loginInfo, buildStream, onlyGoodBuild, "team_scm_snapshotUUID", clientLocale);
		if (LOGGER.isLoggable(Level.FINEST)) {
			LOGGER.finest("Helper.getSnapshotUUIDFromPreviousBuild : " + 
						((snapshotUUID == null) ? "No snapshotUUID found from a previous build" : snapshotUUID));
		}
		return snapshotUUID;
	}
	
	public static String getStreamChangesDataFromLastBuild(final Job<?,?> job, String toolkit, RTCLoginInfo loginInfo, String buildStream, Locale clientLocale) throws Exception {
		if (buildStream == null) {
			return null;
		}
		String streamChangesData = getValueForBuildStream(new IJenkinsBuildIterator() {
			@Override
			public Run<?,?> nextBuild(Run<?, ?> build) {
				return build.getPreviousBuild();
			}
			
			@Override
			public Run<?, ?> firstBuild() {
				return job.getLastBuild();
			}}, toolkit, loginInfo, buildStream, false, "team_scm_streamChangesData", clientLocale);
		if (LOGGER.isLoggable(Level.FINEST)) {
			LOGGER.finest("Helper.getStreamChangesDataFromLastBuild : " + 
						((streamChangesData == null) ? "No stream changes data found from a previous build" : streamChangesData));
		}
		return streamChangesData;
	}

	private static String getValueForBuildStream(IJenkinsBuildIterator iterator, String toolkit, RTCLoginInfo loginInfo, String buildStream, boolean onlyGoodBuild, String key, Locale clientLocale) throws Exception {
		if (buildStream == null) {
			return null;
		}
		Run <?,?> build = iterator.firstBuild();
		String value = null;
		RTCFacadeWrapper facade = RTCFacadeFactory.getFacade(toolkit, null);
		String streamUUID = null;
		while (build != null && value == null) {
			List<RTCBuildResultAction> rtcBuildResultActions = build.getActions(RTCBuildResultAction.class);
			if (rtcBuildResultActions.size() == 1) { // the usual case for freestyle builds (without multiple SCM) and workflow build with only one invocation of RTCScm
				RTCBuildResultAction rtcBuildResultAction = rtcBuildResultActions.get(0);
				if ((rtcBuildResultAction != null) && (rtcBuildResultAction.getBuildProperties() != null)) {
					value = rtcBuildResultAction.getBuildProperties().get(key);
				}
			}
			else {
				if (streamUUID == null) {
					// Resolve the stream and get stream UUID
					streamUUID = (String) facade.invoke("getStreamUUID", new Class[] { //$NON-NLS-1$
							String.class, // serverURI,
							String.class, // userId,
							String.class, // password,
							int.class, // timeout,
							String.class, // buildStream,
							Locale.class // clientLocale
					}, loginInfo.getServerUri(), loginInfo.getUserId(), loginInfo.getPassword(),
							loginInfo.getTimeout(), buildStream, clientLocale);
				}
				for (RTCBuildResultAction rtcBuildResultAction : rtcBuildResultActions) {
					if ((rtcBuildResultAction != null) && (rtcBuildResultAction.getBuildProperties() != null)) {
						Map<String,String> buildProperties = rtcBuildResultAction.getBuildProperties();
						String owningStreamUUID = Util.fixEmptyAndTrim(buildProperties.get("team_scm_snapshotOwner"));
						if (owningStreamUUID == null) {
							continue;
						}
						if (owningStreamUUID.equals(streamUUID)) {
							value = buildProperties.get(key);
						}
					}
				}
			}
			build = iterator.nextBuild(build);
		}
		return value;
	}

	/**
	 * Reads and returns the json text from the specified file
	 * 
	 * @param filePath path to the file on disk containing the json text
	 * @return json text specified in the file
	 * @throws Exception
	 */
	private static String readJsonFromFile(String filePath) throws Exception {
		if (Util.fixEmptyAndTrim(filePath) != null) {
			File fileHandle = new File(filePath);
			if (!fileHandle.exists()) {
				throw new IllegalArgumentException(Messages.Helper_file_not_found(filePath));
			}
			if (!fileHandle.isFile()) {
				throw new IllegalArgumentException(Messages.Helper_not_a_file(filePath));
			}
			InputStream is = new FileInputStream(fileHandle);
			String jsonTxt = IOUtils.toString(is);
			is.close();
			return jsonTxt;
		}
		return null;
	}

	private interface IJenkinsBuildIterator {
		Run<?,?> firstBuild();
		Run<?,?> nextBuild(Run<?,?> build);
	}
}

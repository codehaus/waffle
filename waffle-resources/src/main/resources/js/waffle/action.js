/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */

/**
 * Javascript functions for Waffle web applications
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */

/**
 * Returns a document form by id or the first one in the document if id is null.
 * 
 * @param formId the id of the form
 * @return The document form
 */
function findForm(formId) {
  if(formId == null) {
    return document.forms[0];
  } else {
    return document.getElementById(formId);
  }
}

/**
 * Fires action method for given method name and form id
 *
 * @param methodName the method name
 * @param formId reference to the form id (optional)
 */
function fireActionMethod(methodName, formId) {
  var form = findForm(formId);
  submitInputMethod(form, methodName);
}

/**
 * Fires action method with multipart encoding for given method name and form id
 *
 * @param methodName the method name
 * @param formId reference to the form id (optional)
 */
function fireMultipartActionMethod(methodName, formId) {
  var form = findForm(formId);
  form.method="post";
  form.encoding="multipart/form-data";
  submitInputMethod(form, methodName);
}

/**
 * Submits hidden input form with action method name as attribute
 *
 * @param form the document form to submit
 * @param methodName the method name
 */
function submitInputMethod(form, methodName) {
  var method = document.createElement("input");
  method.setAttribute("type", "hidden");
  method.setAttribute("name", "method");
  method.value = methodName;

  form.appendChild(method);
  form.submit();
  return true;
}
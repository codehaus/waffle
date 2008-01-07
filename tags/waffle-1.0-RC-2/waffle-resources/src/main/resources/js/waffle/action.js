/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/

function findForm(formId) {
  if(formId == null) {
    return document.forms[0];
  } else {
    return document.getElementById(formId);
  }
}

/**
 * @param methodName
 * @param formId reference to the form id (optional)
 */
function fireActionMethod(methodName, formId) {
  var form = findForm(formId);
  submitInputMethod(form, methodName);
}

/**
 * @param methodName
 * @param formId reference to the form id (optional)
 */
function fireMultipartActionMethod(methodName, formId) {
  var form = findForm(formId);
  form.method="post";
  form.encoding="multipart/form-data";
  submitInputMethod(form, methodName);
}

function submitInputMethod(form, methodName) {
  var method = document.createElement("input");
  method.setAttribute("type", "hidden");
  method.setAttribute("name", "method");
  method.value = methodName;

  form.appendChild(method);
  form.submit();
  return true;
}
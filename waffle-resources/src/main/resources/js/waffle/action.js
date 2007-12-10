/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/

function fireActionMethod(methodName) {
	var form = document.forms[0];
    submitInputMethod(form, methodName);
}

function fireMultipartActionMethod(methodName) {
	var form = document.forms[0];
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
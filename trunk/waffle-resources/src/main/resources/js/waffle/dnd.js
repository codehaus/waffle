/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *****************************************************************************/
 
function makeDraggable(className) {
    var items = document.getElementsByClassName(className);
    for (var i = 0; i < items.length; i++) {
        new Draggable(items[i].id, {ghosting:true, revert:true})
    }
}

function handleDrop(element, dropon, event) {
    var pars = 'method=' + element.id;
    new Ajax.Updater({success: 'selection'}, "waffle", {method: 'get', parameters: pars, evalScripts:true});
}
 
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
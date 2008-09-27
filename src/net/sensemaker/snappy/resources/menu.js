sp_menu = {
    expand:function(parent, content){
        var cn = parent.parentNode.childNodes;
        new Effect.Highlight(parent,{duration:0.5});
        var i = 0;
        for(i=0;i<cn.length;i++){
            if(cn[i].nodeName.toLowerCase() == 'div'){
                if(cn[i].style.display == 'none'){
                    Effect.BlindDown(cn[i],{duration:0.25});
                }else{
                   Effect.BlindUp(cn[i],{duration:0.25});
                }
            }else{
                //console.log(cn[i].nodeName);
            }
        }

        if(content){
            // don't send up if this selection will not require a re render.
            var id = parent.id;
            console.log("ID =" + id);
            i = id.indexOf('_SPMI_');
            id = id.substring(0,i);
            i = i + 6;
            var selection = $(id+'_SPSEL');
            var currentSelection = null;
            if(selection)
                currentSelection = selection.innerHTML;
            var inputField = $(id+'_INPUT');
            var newSelection = parent.id.substring(i);
            if(currentSelection != newSelection){
                inputField.value =  newSelection;
                var f = function(){sp_menu.form(parent).submit();};
                Effect.Fade((id+ '_SPC'),{duration:0.25,afterFinish:f});
            }
        }

    },form:function(element){
        var nodeName = element.nodeName.toUpperCase();
        if(nodeName == 'FORM'){return element;}
        if(element.parentNode == null){return null;}
        return sp_menu.form(element.parentNode);
    }
}
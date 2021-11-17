!function(t){function e(e,o,p){var d=this;d.id=p,d.options=o,d.status={animated:!1,rendered:!1,disabled:!1,focused:!1},d.elements={target:e.addClass(d.options.style.classes.target),tooltip:null,wrapper:null,content:null,contentWrapper:null,title:null,button:null,tip:null,bgiframe:null},d.cache={mouse:{},position:{},toggle:0},d.timers={},t.extend(d,d.options.api,{show:function(e){var o;if(!d.status.rendered)return t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"show");if("none"!==d.elements.tooltip.css("display"))return d;if(d.elements.tooltip.stop(!0,!1),!1===d.beforeShow.call(d,e))return d;function i(){"static"!==d.options.position.type&&d.focus(),d.onShow.call(d,e),t.browser.msie&&d.elements.tooltip.get(0).style.removeAttribute("filter")}if(d.cache.toggle=1,"static"!==d.options.position.type&&d.updatePosition(e,d.options.show.effect.length>0),"object"==typeof d.options.show.solo?o=t(d.options.show.solo):!0===d.options.show.solo&&(o=t("div.qtip").not(d.elements.tooltip)),o&&o.each(function(){!0===t(this).qtip("api").status.rendered&&t(this).qtip("api").hide()}),"function"==typeof d.options.show.effect.type)d.options.show.effect.type.call(d.elements.tooltip,d.options.show.effect.length),d.elements.tooltip.queue(function(){i(),t(this).dequeue()});else{switch(d.options.show.effect.type.toLowerCase()){case"fade":d.elements.tooltip.fadeIn(d.options.show.effect.length,i);break;case"slide":d.elements.tooltip.slideDown(d.options.show.effect.length,function(){i(),"static"!==d.options.position.type&&d.updatePosition(e,!0)});break;case"grow":d.elements.tooltip.show(d.options.show.effect.length,i);break;default:d.elements.tooltip.show(null,i)}d.elements.tooltip.addClass(d.options.style.classes.active)}return t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_SHOWN,"show")},hide:function(e){if(!d.status.rendered)return t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"hide");if("none"===d.elements.tooltip.css("display"))return d;if(clearTimeout(d.timers.show),d.elements.tooltip.stop(!0,!1),!1===d.beforeHide.call(d,e))return d;function o(){d.onHide.call(d,e)}if(d.cache.toggle=0,"function"==typeof d.options.hide.effect.type)d.options.hide.effect.type.call(d.elements.tooltip,d.options.hide.effect.length),d.elements.tooltip.queue(function(){o(),t(this).dequeue()});else{switch(d.options.hide.effect.type.toLowerCase()){case"fade":d.elements.tooltip.fadeOut(d.options.hide.effect.length,o);break;case"slide":d.elements.tooltip.slideUp(d.options.hide.effect.length,o);break;case"grow":d.elements.tooltip.hide(d.options.hide.effect.length,o);break;default:d.elements.tooltip.hide(null,o)}d.elements.tooltip.removeClass(d.options.style.classes.active)}return t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_HIDDEN,"hide")},updatePosition:function(e,o){var i,n,r,l,p,a,h,c,f,u,m;if(!d.status.rendered)return t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"updatePosition");if("static"==d.options.position.type)return t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.CANNOT_POSITION_STATIC,"updatePosition");if(n={position:{left:0,top:0},dimensions:{height:0,width:0},corner:d.options.position.corner.target},r={position:d.getPosition(),dimensions:d.getDimensions(),corner:d.options.position.corner.tooltip},"mouse"!==d.options.position.target){if("area"==d.options.position.target.get(0).nodeName.toLowerCase()){for(l=d.options.position.target.attr("coords").split(","),i=0;i<l.length;i++)l[i]=parseInt(l[i]);switch(p=d.options.position.target.parent("map").attr("name"),a=t('img[usemap="#'+p+'"]:first').offset(),n.position={left:Math.floor(a.left+l[0]),top:Math.floor(a.top+l[1])},d.options.position.target.attr("shape").toLowerCase()){case"rect":n.dimensions={width:Math.ceil(Math.abs(l[2]-l[0])),height:Math.ceil(Math.abs(l[3]-l[1]))};break;case"circle":n.dimensions={width:l[2]+1,height:l[2]+1};break;case"poly":for(n.dimensions={width:l[0],height:l[1]},i=0;i<l.length;i++)i%2==0?(l[i]>n.dimensions.width&&(n.dimensions.width=l[i]),l[i]<l[0]&&(n.position.left=Math.floor(a.left+l[i]))):(l[i]>n.dimensions.height&&(n.dimensions.height=l[i]),l[i]<l[1]&&(n.position.top=Math.floor(a.top+l[i])));n.dimensions.width=n.dimensions.width-(n.position.left-a.left),n.dimensions.height=n.dimensions.height-(n.position.top-a.top);break;default:return t.fn.qtip.log.error.call(d,4,t.fn.qtip.constants.INVALID_AREA_SHAPE,"updatePosition")}n.dimensions.width-=2,n.dimensions.height-=2}else 1===d.options.position.target.add(document.body).length?(n.position={left:t(document).scrollLeft(),top:t(document).scrollTop()},n.dimensions={height:t(window).height(),width:t(window).width()}):(void 0!==d.options.position.target.attr("qtip")?n.position=d.options.position.target.qtip("api").cache.position:n.position=d.options.position.target.offset(),n.dimensions={height:d.options.position.target.outerHeight(),width:d.options.position.target.outerWidth()});h=t.extend({},n.position),-1!==n.corner.search(/right/i)&&(h.left+=n.dimensions.width),-1!==n.corner.search(/bottom/i)&&(h.top+=n.dimensions.height),-1!==n.corner.search(/((top|bottom)Middle)|center/)&&(h.left+=n.dimensions.width/2),-1!==n.corner.search(/((left|right)Middle)|center/)&&(h.top+=n.dimensions.height/2)}else n.position=h={left:d.cache.mouse.x,top:d.cache.mouse.y},n.dimensions={height:1,width:1};if(-1!==r.corner.search(/right/i)&&(h.left-=r.dimensions.width),-1!==r.corner.search(/bottom/i)&&(h.top-=r.dimensions.height),-1!==r.corner.search(/((top|bottom)Middle)|center/)&&(h.left-=r.dimensions.width/2),-1!==r.corner.search(/((left|right)Middle)|center/)&&(h.top-=r.dimensions.height/2),c=t.browser.msie?1:0,t.browser.msie&&6===parseInt(t.browser.version.charAt(0))?1:0,d.options.style.border.radius>0&&(-1!==r.corner.search(/Left/)?h.left-=d.options.style.border.radius:-1!==r.corner.search(/Right/)&&(h.left+=d.options.style.border.radius),-1!==r.corner.search(/Top/)?h.top-=d.options.style.border.radius:-1!==r.corner.search(/Bottom/)&&(h.top+=d.options.style.border.radius)),c&&(-1!==r.corner.search(/top/)?h.top-=c:-1!==r.corner.search(/bottom/)&&(h.top+=c),-1!==r.corner.search(/left/)?h.left-=c:-1!==r.corner.search(/right/)&&(h.left+=c),-1!==r.corner.search(/leftMiddle|rightMiddle/)&&(h.top-=1)),!0===d.options.position.adjust.screen&&(h=function(e,o,i){var n,r,l,p;if(this,"center"==i.corner)return o.position;n=t.extend({},e),l={x:!1,y:!1},p={left:n.left<t.fn.qtip.cache.screen.scroll.left,right:n.left+i.dimensions.width+2>=t.fn.qtip.cache.screen.width+t.fn.qtip.cache.screen.scroll.left,top:n.top<t.fn.qtip.cache.screen.scroll.top,bottom:n.top+i.dimensions.height+2>=t.fn.qtip.cache.screen.height+t.fn.qtip.cache.screen.scroll.top},(r={left:p.left&&(-1!=i.corner.search(/right/i)||-1==i.corner.search(/right/i)&&!p.right),right:p.right&&(-1!=i.corner.search(/left/i)||-1==i.corner.search(/left/i)&&!p.left),top:p.top&&-1==i.corner.search(/top/i),bottom:p.bottom&&-1==i.corner.search(/bottom/i)}).left?("mouse"!==this.options.position.target?n.left=o.position.left+o.dimensions.width:n.left=this.cache.mouse.x,l.x="Left"):r.right&&("mouse"!==this.options.position.target?n.left=o.position.left-i.dimensions.width:n.left=this.cache.mouse.x-i.dimensions.width,l.x="Right");r.top?("mouse"!==this.options.position.target?n.top=o.position.top+o.dimensions.height:n.top=this.cache.mouse.y,l.y="top"):r.bottom&&("mouse"!==this.options.position.target?n.top=o.position.top-i.dimensions.height:n.top=this.cache.mouse.y-i.dimensions.height,l.y="bottom");n.left<0&&(n.left=e.left,l.x=!1);n.top<0&&(n.top=e.top,l.y=!1);!1!==this.options.style.tip.corner&&(n.corner=new String(i.corner),!1!==l.x&&(n.corner=n.corner.replace(/Left|Right|Middle/,l.x)),!1!==l.y&&(n.corner=n.corner.replace(/top|bottom/,l.y)),n.corner!==this.elements.tip.attr("rel")&&s.call(this,n.corner));return n}.call(d,h,n,r)),"mouse"===d.options.position.target&&!0===d.options.position.adjust.mouse&&(f=!0===d.options.position.adjust.screen&&d.elements.tip?d.elements.tip.attr("rel"):d.options.position.corner.tooltip,h.left+=-1!==f.search(/right/i)?-6:6,h.top+=-1!==f.search(/bottom/i)?-6:6),!d.elements.bgiframe&&t.browser.msie&&6==parseInt(t.browser.version.charAt(0))&&t("select, object").each(function(){(u=t(this).offset()).bottom=u.top+t(this).height(),u.right=u.left+t(this).width(),h.top+r.dimensions.height>=u.top&&h.left+r.dimensions.width>=u.left&&function(){var t,e;e=this.getDimensions(),t='<iframe class="qtip-bgiframe" style="border:0;" tabindex="-1" src="javascript:false" style="display:block; position:absolute; z-index:-1; filter:alpha(opacity=\'0\'); border: 1px solid red; height:'+e.height+"px; width:"+e.width+'px" />',this.elements.bgiframe=this.elements.wrapper.prepend(t).children(".qtip-bgiframe:first")}.call(d)}),h.left+=d.options.position.adjust.x,h.top+=d.options.position.adjust.y,m=d.getPosition(),h.left!=m.left||h.top!=m.top){if(!1===d.beforePositionUpdate.call(d,e))return d;d.cache.position=h,!0===o?(d.status.animated=!0,d.elements.tooltip.animate(h,200,"swing",function(){d.status.animated=!1})):d.elements.tooltip.css(h),d.onPositionUpdate.call(d,e),void 0!==e&&e.type&&"mousemove"!==e.type&&t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_POSITION_UPDATED,"updatePosition")}return d},updateWidth:function(e){var o;return d.status.rendered?e&&"number"!=typeof e?t.fn.qtip.log.error.call(d,2,"newWidth must be of type number","updateWidth"):(o=d.elements.contentWrapper.siblings().add(d.elements.tip).add(d.elements.button),e||("number"==typeof d.options.style.width.value?e=d.options.style.width.value:(d.elements.tooltip.css({width:"auto"}),o.hide(),t.browser.msie&&d.elements.wrapper.add(d.elements.contentWrapper.children()).css({zoom:"normal"}),e=d.getDimensions().width+1,d.options.style.width.value||(e>d.options.style.width.max&&(e=d.options.style.width.max),e<d.options.style.width.min&&(e=d.options.style.width.min)))),e%2!=0&&(e-=1),d.elements.tooltip.width(e),o.show(),d.options.style.border.radius&&d.elements.tooltip.find(".qtip-betweenCorners").each(function(o){t(this).width(e-2*d.options.style.border.radius)}),t.browser.msie&&(d.elements.wrapper.add(d.elements.contentWrapper.children()).css({zoom:"1"}),d.elements.wrapper.width(e),d.elements.bgiframe&&d.elements.bgiframe.width(e).height(d.getDimensions.height)),t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_WIDTH_UPDATED,"updateWidth")):t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"updateWidth")},updateStyle:function(e){var o,s,r,p;return d.status.rendered?"string"==typeof e&&t.fn.qtip.styles[e]?(d.options.style=a.call(d,t.fn.qtip.styles[e],d.options.user.style),d.elements.content.css(l(d.options.style)),!1!==d.options.content.title.text&&d.elements.title.css(l(d.options.style.title,!0)),d.elements.contentWrapper.css({borderColor:d.options.style.border.color}),!1!==d.options.style.tip.corner&&(t("<canvas>").get(0).getContext?(o=d.elements.tooltip.find(".qtip-tip canvas:first"),o.get(0).getContext("2d").clearRect(0,0,300,300),p=h(r=o.parent("div[rel]:first").attr("rel"),d.options.style.tip.size.width,d.options.style.tip.size.height),n.call(d,o,p,d.options.style.tip.color||d.options.style.border.color)):t.browser.msie&&(o=d.elements.tooltip.find('.qtip-tip [nodeName="shape"]')).attr("fillcolor",d.options.style.tip.color||d.options.style.border.color)),d.options.style.border.radius>0&&(d.elements.tooltip.find(".qtip-betweenCorners").css({backgroundColor:d.options.style.border.color}),t("<canvas>").get(0).getContext?(s=c(d.options.style.border.radius),d.elements.tooltip.find(".qtip-wrapper canvas").each(function(){t(this).get(0).getContext("2d").clearRect(0,0,300,300),r=t(this).parent("div[rel]:first").attr("rel"),i.call(d,t(this),s[r],d.options.style.border.radius,d.options.style.border.color)})):t.browser.msie&&d.elements.tooltip.find('.qtip-wrapper [nodeName="arc"]').each(function(){t(this).attr("fillcolor",d.options.style.border.color)})),t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_STYLE_UPDATED,"updateStyle")):t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.STYLE_NOT_DEFINED,"updateStyle"):t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"updateStyle")},updateContent:function(e,o){var i,s,n;if(!d.status.rendered)return t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"updateContent");if(!e)return t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.NO_CONTENT_PROVIDED,"updateContent");if("string"==typeof(i=d.beforeContentUpdate.call(d,e)))e=i;else if(!1===i)return;function l(){d.updateWidth(),!1!==o&&("static"!==d.options.position.type&&d.updatePosition(d.elements.tooltip.is(":visible"),!0),!1!==d.options.style.tip.corner&&r.call(d))}return t.browser.msie&&d.elements.contentWrapper.children().css({zoom:"normal"}),e.jquery&&e.length>0?e.clone(!0).appendTo(d.elements.content).show():d.elements.content.html(e),(s=d.elements.content.find("img[complete=false]")).length>0?(n=0,s.each(function(e){t('<img src="'+t(this).attr("src")+'" />').load(function(){++n==s.length&&l()})})):l(),d.onContentUpdate.call(d),t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_CONTENT_UPDATED,"loadContent")},loadContent:function(e,o,i){if(!d.status.rendered)return t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"loadContent");if(!1===d.beforeContentLoad.call(d))return d;function s(e){d.onContentLoad.call(d),t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_CONTENT_LOADED,"loadContent"),d.updateContent(e)}return"post"==i?t.post(e,o,s):t.get(e,o,s),d},updateTitle:function(e){if(!d.status.rendered)return t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"updateTitle");if(!e)return t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.NO_CONTENT_PROVIDED,"updateTitle");return!1===d.beforeTitleUpdate.call(d)?d:(d.elements.button&&(d.elements.button=d.elements.button.clone(!0)),d.elements.title.html(e),d.elements.button&&d.elements.title.prepend(d.elements.button),d.onTitleUpdate.call(d),t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_TITLE_UPDATED,"updateTitle"))},focus:function(e){var o,i,s;if(!d.status.rendered)return t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"focus");if("static"==d.options.position.type)return t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.CANNOT_FOCUS_STATIC,"focus");if(o=parseInt(d.elements.tooltip.css("z-index")),i=6e3+t("div.qtip[qtip]").length-1,!d.status.focused&&o!==i){if(!1===d.beforeFocus.call(d,e))return d;t("div.qtip[qtip]").not(d.elements.tooltip).each(function(){!0===t(this).qtip("api").status.rendered&&("number"==typeof(s=parseInt(t(this).css("z-index")))&&s>-1&&t(this).css({zIndex:parseInt(t(this).css("z-index"))-1}),t(this).qtip("api").status.focused=!1)}),d.elements.tooltip.css({zIndex:i}),d.status.focused=!0,d.onFocus.call(d,e),t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_FOCUSED,"focus")}return d},disable:function(e){return d.status.rendered?(e?d.status.disabled?t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.TOOLTIP_ALREADY_DISABLED,"disable"):(d.status.disabled=!0,t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_DISABLED,"disable")):d.status.disabled?(d.status.disabled=!1,t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_ENABLED,"disable")):t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.TOOLTIP_ALREADY_ENABLED,"disable"),d):t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"disable")},destroy:function(){var e,o;if(!1===d.beforeDestroy.call(d))return d;if(d.status.rendered?(d.options.show.when.target.off("mousemove.qtip",d.updatePosition),d.options.show.when.target.off("mouseout.qtip",d.hide),d.options.show.when.target.off(d.options.show.when.event+".qtip"),d.options.hide.when.target.off(d.options.hide.when.event+".qtip"),d.elements.tooltip.off(d.options.hide.when.event+".qtip"),d.elements.tooltip.off("mouseover.qtip",d.focus),d.elements.tooltip.remove()):d.options.show.when.target.off(d.options.show.when.event+".qtip-create"),"object"==typeof d.elements.target.data("qtip")&&"object"==typeof(o=d.elements.target.data("qtip").interfaces)&&o.length>0)for(e=0;e<o.length-1;e++)o[e].id==d.id&&o.splice(e,1);return delete t.fn.qtip.interfaces[d.id],"object"==typeof o&&o.length>0?d.elements.target.data("qtip").current=o.length-1:d.elements.target.removeData("qtip"),d.onDestroy.call(d),t.fn.qtip.log.error.call(d,1,t.fn.qtip.constants.EVENT_DESTROYED,"destroy"),d.elements.target},getPosition:function(){var e,o;return d.status.rendered?((e="none"===d.elements.tooltip.css("display"))&&d.elements.tooltip.css({visiblity:"hidden"}).show(),o=d.elements.tooltip.offset(),e&&d.elements.tooltip.css({visiblity:"visible"}).hide(),o):t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"getPosition")},getDimensions:function(){var e,o;return d.status.rendered?((e=!d.elements.tooltip.is(":visible"))&&d.elements.tooltip.css({visiblity:"hidden"}).show(),o={height:d.elements.tooltip.outerHeight(),width:d.elements.tooltip.outerWidth()},e&&d.elements.tooltip.css({visiblity:"visible"}).hide(),o):t.fn.qtip.log.error.call(d,2,t.fn.qtip.constants.TOOLTIP_NOT_RENDERED,"getDimensions")}})}function o(){var e,o,n,r;this.beforeRender.call(this),this.status.rendered=!0,this.elements.tooltip='<div qtip="'+this.id+'" class="qtip '+(this.options.style.classes.tooltip||this.options.style)+'" style="display:none; -moz-border-radius:0; -webkit-border-radius:0; border-radius:0;position:'+this.options.position.type+';">  <div class="qtip-wrapper" style="position:relative; overflow:hidden; text-align:left;">    <div class="qtip-contentWrapper" style="overflow:hidden;">       <div class="qtip-content '+this.options.style.classes.content+'"></div></div></div></div>',this.elements.tooltip=t(this.elements.tooltip),this.elements.tooltip.appendTo(this.options.position.container),this.elements.tooltip.data("qtip",{current:0,interfaces:[this]}),this.elements.wrapper=this.elements.tooltip.children("div:first"),this.elements.contentWrapper=this.elements.wrapper.children("div:first").css({background:this.options.style.background}),this.elements.content=this.elements.contentWrapper.children("div:first").css(l(this.options.style)),t.browser.msie&&this.elements.wrapper.add(this.elements.content).css({zoom:1}),"unfocus"==this.options.hide.when.event&&this.elements.tooltip.attr("unfocus",!0),"number"==typeof this.options.style.width.value&&this.updateWidth(),t("<canvas>").get(0).getContext||t.browser.msie?(this.options.style.border.radius>0?function(){var e,o,s,n,r,l,p,a,h,d,f,u,m,g,w;for(o in(e=this).elements.wrapper.find(".qtip-borderBottom, .qtip-borderTop").remove(),s=e.options.style.border.width,n=e.options.style.border.radius,r=e.options.style.border.color||e.options.style.tip.color,l=c(n),p={},l)p[o]='<div rel="'+o+'" style="'+(-1!==o.search(/Left/)?"left":"right")+":0; position:absolute; height:"+n+"px; width:"+n+'px; overflow:hidden; line-height:0.1px; font-size:1px">',t("<canvas>").get(0).getContext?p[o]+='<canvas height="'+n+'" width="'+n+'" style="vertical-align: top"></canvas>':t.browser.msie&&(a=2*n+3,p[o]+='<v:arc stroked="false" fillcolor="'+r+'" startangle="'+l[o][0]+'" endangle="'+l[o][1]+'" style="width:'+a+"px; height:"+a+"px; margin-top:"+(-1!==o.search(/bottom/)?-2:-1)+"px; margin-left:"+(-1!==o.search(/Right/)?l[o][2]-3.5:-1)+'px; vertical-align:top; display:inline-block; behavior:url(#default#VML)"></v:arc>'),p[o]+="</div>";h=e.getDimensions().width-2*Math.max(s,n),d='<div class="qtip-betweenCorners" style="height:'+n+"px; width:"+h+"px; overflow:hidden; background-color:"+r+'; line-height:0.1px; font-size:1px;">',f='<div class="qtip-borderTop" dir="ltr" style="height:'+n+"px; margin-left:"+n+'px; line-height:0.1px; font-size:1px; padding:0;">'+p.topLeft+p.topRight+d,e.elements.wrapper.prepend(f),u='<div class="qtip-borderBottom" dir="ltr" style="height:'+n+"px; margin-left:"+n+'px; line-height:0.1px; font-size:1px; padding:0;">'+p.bottomLeft+p.bottomRight+d,e.elements.wrapper.append(u),t("<canvas>").get(0).getContext?e.elements.wrapper.find("canvas").each(function(){m=l[t(this).parent("[rel]:first").attr("rel")],i.call(e,t(this),m,n,r)}):t.browser.msie&&e.elements.tooltip.append('<v:image style="behavior:url(#default#VML);"></v:image>');g=Math.max(n,n+(s-n)),w=Math.max(s-n,0),e.elements.contentWrapper.css({border:"0px solid "+r,borderWidth:w+"px "+g+"px"})}.call(this):this.elements.contentWrapper.css({border:this.options.style.border.width+"px solid "+this.options.style.border.color}),!1!==this.options.style.tip.corner&&s.call(this)):(this.elements.contentWrapper.css({border:this.options.style.border.width+"px solid "+this.options.style.border.color}),this.options.style.border.radius=0,this.options.style.tip.corner=!1,t.fn.qtip.log.error.call(this,2,t.fn.qtip.constants.CANVAS_VML_NOT_SUPPORTED,"render")),"string"==typeof this.options.content.text&&this.options.content.text.length>0||this.options.content.text.jquery&&this.options.content.text.length>0?e=this.options.content.text:"string"==typeof this.elements.target.attr("title")&&this.elements.target.attr("title").length>0?(e=this.elements.target.attr("title").replace("\\n","<br />"),this.elements.target.attr("title","")):"string"==typeof this.elements.target.attr("alt")&&this.elements.target.attr("alt").length>0?(e=this.elements.target.attr("alt").replace("\\n","<br />"),this.elements.target.attr("alt","")):(e=" ",t.fn.qtip.log.error.call(this,1,t.fn.qtip.constants.NO_VALID_CONTENT,"render")),!1!==this.options.content.title.text&&function(){var e=this;null!==e.elements.title&&e.elements.title.remove();e.elements.title=t('<div class="'+e.options.style.classes.title+'">').css(l(e.options.style.title,!0)).css({zoom:t.browser.msie?1:0}).prependTo(e.elements.contentWrapper),e.options.content.title.text&&e.updateTitle.call(e,e.options.content.title.text);!1!==e.options.content.title.button&&"string"==typeof e.options.content.title.button&&(e.elements.button=t('<a class="'+e.options.style.classes.button+'" style="float:right; position: relative"></a>').css(l(e.options.style.button,!0)).html(e.options.content.title.button).prependTo(e.elements.title).click(function(t){e.status.disabled||e.hide(t)}))}.call(this),this.updateContent(e),function(){var e,o,i,s;o=(e=this).options.show.when.target,i=e.options.hide.when.target,e.options.hide.fixed&&(i=i.add(e.elements.tooltip));if("inactive"==e.options.hide.when.event){function n(o){!0!==e.status.disabled&&(clearTimeout(e.timers.inactive),e.timers.inactive=setTimeout(function(){t(s).each(function(){i.off(this+".qtip-inactive"),e.elements.content.off(this+".qtip-inactive")}),e.hide(o)},e.options.hide.delay))}s=["click","dblclick","mousedown","mouseup","mousemove","mouseout","mouseenter","mouseleave","mouseover"]}else!0===e.options.hide.fixed&&e.elements.tooltip.on("mouseover.qtip",function(){!0!==e.status.disabled&&clearTimeout(e.timers.hide)});function r(o){!0!==e.status.disabled&&("inactive"==e.options.hide.when.event&&(t(s).each(function(){i.on(this+".qtip-inactive",n),e.elements.content.on(this+".qtip-inactive",n)}),n()),clearTimeout(e.timers.show),clearTimeout(e.timers.hide),e.timers.show=setTimeout(function(){e.show(o)},e.options.show.delay))}function l(o){if(!0!==e.status.disabled){if(!0===e.options.hide.fixed&&-1!==e.options.hide.when.event.search(/mouse(out|leave)/i)&&t(o.relatedTarget).parents("div.qtip[qtip]").length>0)return o.stopPropagation(),o.preventDefault(),clearTimeout(e.timers.hide),!1;clearTimeout(e.timers.show),clearTimeout(e.timers.hide),e.elements.tooltip.stop(!0,!0),e.timers.hide=setTimeout(function(){e.hide(o)},e.options.hide.delay)}}1===e.options.show.when.target.add(e.options.hide.when.target).length&&e.options.show.when.event==e.options.hide.when.event&&"inactive"!==e.options.hide.when.event||"unfocus"==e.options.hide.when.event?(e.cache.toggle=0,o.on(e.options.show.when.event+".qtip",function(t){0==e.cache.toggle?r(t):l(t)})):(o.on(e.options.show.when.event+".qtip",r),"inactive"!==e.options.hide.when.event&&i.on(e.options.hide.when.event+".qtip",l));-1!==e.options.position.type.search(/(fixed|absolute)/)&&e.elements.tooltip.on("mouseover.qtip",e.focus);"mouse"===e.options.position.target&&"static"!==e.options.position.type&&o.on("mousemove.qtip",function(t){e.cache.mouse={x:t.pageX,y:t.pageY},!1===e.status.disabled&&!0===e.options.position.adjust.mouse&&"static"!==e.options.position.type&&"none"!==e.elements.tooltip.css("display")&&e.updatePosition(t)})}.call(this),!0===this.options.show.ready&&this.show(),!1!==this.options.content.url&&(o=this.options.content.url,n=this.options.content.data,r=this.options.content.method||"get",this.loadContent(o,n,r)),this.onRender.call(this),t.fn.qtip.log.error.call(this,1,t.fn.qtip.constants.EVENT_RENDERED,"render")}function i(t,e,o,i){var s=t.get(0).getContext("2d");s.fillStyle=i,s.beginPath(),s.arc(e[0],e[1],o,0,2*Math.PI,!1),s.fill()}function s(e){var o,i,s,l;null!==this.elements.tip&&this.elements.tip.remove(),o=this.options.style.tip.color||this.options.style.border.color,!1!==this.options.style.tip.corner&&(e||(e=this.options.style.tip.corner),i=h(e,this.options.style.tip.size.width,this.options.style.tip.size.height),this.elements.tip='<div class="'+this.options.style.classes.tip+'" dir="ltr" rel="'+e+'" style="position:absolute; height:'+this.options.style.tip.size.height+"px; width:"+this.options.style.tip.size.width+'px; margin:0 auto; line-height:0.1px; font-size:1px;">',t("<canvas>").get(0).getContext?this.elements.tip+='<canvas height="'+this.options.style.tip.size.height+'" width="'+this.options.style.tip.size.width+'"></canvas>':t.browser.msie&&(s=this.options.style.tip.size.width+","+this.options.style.tip.size.height,l="m"+i[0][0]+","+i[0][1],l+=" l"+i[1][0]+","+i[1][1],l+=" "+i[2][0]+","+i[2][1],l+=" xe",this.elements.tip+='<v:shape fillcolor="'+o+'" stroked="false" filled="true" path="'+l+'" coordsize="'+s+'" style="width:'+this.options.style.tip.size.width+"px; height:"+this.options.style.tip.size.height+"px; line-height:0.1px; display:inline-block; behavior:url(#default#VML); vertical-align:"+(-1!==e.search(/top/)?"bottom":"top")+'"></v:shape>',this.elements.tip+='<v:image style="behavior:url(#default#VML);"></v:image>',this.elements.contentWrapper.css("position","relative")),this.elements.tooltip.prepend(this.elements.tip+"</div>"),this.elements.tip=this.elements.tooltip.find("."+this.options.style.classes.tip).eq(0),t("<canvas>").get(0).getContext&&n.call(this,this.elements.tip.find("canvas:first"),i,o),-1!==e.search(/top/)&&t.browser.msie&&6===parseInt(t.browser.version.charAt(0))&&this.elements.tip.css({marginTop:-4}),r.call(this,e))}function n(t,e,o){var i=t.get(0).getContext("2d");i.fillStyle=o,i.beginPath(),i.moveTo(e[0][0],e[0][1]),i.lineTo(e[1][0],e[1][1]),i.lineTo(e[2][0],e[2][1]),i.fill()}function r(e){var o,i,s,n;!1!==this.options.style.tip.corner&&this.elements.tip&&(e||(e=this.elements.tip.attr("rel")),o=positionAdjust=t.browser.msie?1:0,this.elements.tip.css(e.match(/left|right|top|bottom/)[0],0),-1!==e.search(/top|bottom/)?(t.browser.msie&&(6===parseInt(t.browser.version.charAt(0))?positionAdjust=-1!==e.search(/top/)?-3:1:positionAdjust=-1!==e.search(/top/)?1:2),-1!==e.search(/Middle/)?this.elements.tip.css({left:"50%",marginLeft:-this.options.style.tip.size.width/2}):-1!==e.search(/Left/)?this.elements.tip.css({left:this.options.style.border.radius-o}):-1!==e.search(/Right/)&&this.elements.tip.css({right:this.options.style.border.radius+o}),-1!==e.search(/top/)?this.elements.tip.css({top:-positionAdjust}):this.elements.tip.css({bottom:positionAdjust})):-1!==e.search(/left|right/)&&(t.browser.msie&&(positionAdjust=6===parseInt(t.browser.version.charAt(0))?1:-1!==e.search(/left/)?1:2),-1!==e.search(/Middle/)?this.elements.tip.css({top:"50%",marginTop:-this.options.style.tip.size.height/2}):-1!==e.search(/Top/)?this.elements.tip.css({top:this.options.style.border.radius-o}):-1!==e.search(/Bottom/)&&this.elements.tip.css({bottom:this.options.style.border.radius+o}),-1!==e.search(/left/)?this.elements.tip.css({left:-positionAdjust}):this.elements.tip.css({right:positionAdjust})),i="padding-"+e.match(/left|right|top|bottom/)[0],s=this.options.style.tip.size[-1!==i.search(/left|right/)?"width":"height"],this.elements.tooltip.css("padding",0),this.elements.tooltip.css(i,s),t.browser.msie&&6==parseInt(t.browser.version.charAt(0))&&(n=parseInt(this.elements.tip.css("margin-top"))||0,n+=parseInt(this.elements.content.css("margin-top"))||0,this.elements.tip.css({marginTop:n})))}function l(e,o){var i,s;for(s in i=t.extend(!0,{},e))!0===o&&-1!==s.search(/(tip|classes)/i)?delete i[s]:o||-1===s.search(/(width|border|tip|title|classes|user)/i)||delete i[s];return i}function p(t){return"object"!=typeof t.tip&&(t.tip={corner:t.tip}),"object"!=typeof t.tip.size&&(t.tip.size={width:t.tip.size,height:t.tip.size}),"object"!=typeof t.border&&(t.border={width:t.border}),"object"!=typeof t.width&&(t.width={value:t.width}),"string"==typeof t.width.max&&(t.width.max=parseInt(t.width.max.replace(/([0-9]+)/i,"$1"))),"string"==typeof t.width.min&&(t.width.min=parseInt(t.width.min.replace(/([0-9]+)/i,"$1"))),"number"==typeof t.tip.size.x&&(t.tip.size.width=t.tip.size.x,delete t.tip.size.x),"number"==typeof t.tip.size.y&&(t.tip.size.height=t.tip.size.y,delete t.tip.size.y),t}function a(){var e,o,i,s,n;for(this,o=[!0,{}],e=0;e<arguments.length;e++)o.push(arguments[e]);for(i=[t.extend.apply(t,o)];"string"==typeof i[0].name;)i.unshift(p(t.fn.qtip.styles[i[0].name]));return i.unshift(!0,{classes:{tooltip:"qtip-"+(arguments[0].name||"defaults")}},t.fn.qtip.styles.defaults),s=t.extend.apply(t,i),n=t.browser.msie?1:0,s.tip.size.width+=n,s.tip.size.height+=n,s.tip.size.width%2>0&&(s.tip.size.width+=1),s.tip.size.height%2>0&&(s.tip.size.height+=1),!0===s.tip.corner&&(s.tip.corner="center"!==this.options.position.corner.tooltip&&this.options.position.corner.tooltip),s}function h(t,e,o){var i={bottomRight:[[0,0],[e,o],[e,0]],bottomLeft:[[0,0],[e,0],[0,o]],topRight:[[0,o],[e,0],[e,o]],topLeft:[[0,0],[0,o],[e,o]],topMiddle:[[0,o],[e/2,0],[e,o]],bottomMiddle:[[0,0],[e,0],[e/2,o]],rightMiddle:[[0,0],[e,o/2],[0,o]],leftMiddle:[[e,0],[e,o],[0,o/2]]};return i.leftTop=i.bottomRight,i.rightTop=i.bottomLeft,i.leftBottom=i.topRight,i.rightBottom=i.topLeft,i[t]}function c(e){var o;return t("<canvas>").get(0).getContext?o={topLeft:[e,e],topRight:[0,e],bottomLeft:[e,0],bottomRight:[0,0]}:t.browser.msie&&(o={topLeft:[-90,90,0],topRight:[-90,90,-e],bottomLeft:[90,270,0],bottomRight:[90,270,-e]}),o}t.fn.qtip=function(i,s){var n,r,l,h,c,d,f,u;if("string"==typeof i){if("object"!=typeof t(this).data("qtip")&&t.fn.qtip.log.error.call(self,1,t.fn.qtip.constants.NO_TOOLTIP_PRESENT,!1),"api"==i)return t(this).data("qtip").interfaces[t(this).data("qtip").current];if("interfaces"==i)return t(this).data("qtip").interfaces}else i||(i={}),("object"!=typeof i.content||i.content.jquery&&i.content.length>0)&&(i.content={text:i.content}),"object"!=typeof i.content.title&&(i.content.title={text:i.content.title}),"object"!=typeof i.position&&(i.position={corner:i.position}),"object"!=typeof i.position.corner&&(i.position.corner={target:i.position.corner,tooltip:i.position.corner}),"object"!=typeof i.show&&(i.show={when:i.show}),"object"!=typeof i.show.when&&(i.show.when={event:i.show.when}),"object"!=typeof i.show.effect&&(i.show.effect={type:i.show.effect}),"object"!=typeof i.hide&&(i.hide={when:i.hide}),"object"!=typeof i.hide.when&&(i.hide.when={event:i.hide.when}),"object"!=typeof i.hide.effect&&(i.hide.effect={type:i.hide.effect}),"object"!=typeof i.style&&(i.style={name:i.style}),i.style=p(i.style),(h=t.extend(!0,{},t.fn.qtip.defaults,i)).style=a.call({options:h},h.style),h.user=t.extend(!0,{},i);return t(this).each(function(){if("string"==typeof i){if(d=i.toLowerCase(),"object"==typeof(l=t(this).qtip("interfaces")))if(!0===s&&"destroy"==d)for(;l.length>0;)l[l.length-1].destroy();else for(!0!==s&&(l=[t(this).qtip("api")]),n=0;n<l.length;n++)"destroy"==d?l[n].destroy():!0===l[n].status.rendered&&("show"==d?l[n].show():"hide"==d?l[n].hide():"focus"==d?l[n].focus():"disable"==d?l[n].disable(!0):"enable"==d&&l[n].disable(!1))}else{for((f=t.extend(!0,{},h)).hide.effect.length=h.hide.effect.length,f.show.effect.length=h.show.effect.length,!1===f.position.container&&(f.position.container=t(document.body)),!1===f.position.target&&(f.position.target=t(this)),!1===f.show.when.target&&(f.show.when.target=t(this)),!1===f.hide.when.target&&(f.hide.when.target=t(this)),r=t.fn.qtip.interfaces.length,n=0;n<r;n++)if(void 0===t.fn.qtip.interfaces[n]){r=n;break}c=new e(t(this),f,r),t.fn.qtip.interfaces[r]=c,"object"==typeof t(this).data("qtip")?(void 0===t(this).attr("qtip")&&(t(this).data("qtip").current=t(this).data("qtip").interfaces.length),t(this).data("qtip").interfaces.push(c)):t(this).data("qtip",{current:0,interfaces:[c]}),!1===f.content.prerender&&!1!==f.show.when.event&&!0!==f.show.ready?f.show.when.target.on(f.show.when.event+".qtip-"+r+"-create",{qtip:r},function(e){(u=t.fn.qtip.interfaces[e.data.qtip]).options.show.when.target.off(u.options.show.when.event+".qtip-"+e.data.qtip+"-create"),u.cache.mouse={x:e.pageX,y:e.pageY},o.call(u),u.options.show.when.target.trigger(u.options.show.when.event)}):(c.cache.mouse={x:f.show.when.target.offset().left,y:f.show.when.target.offset().top},o.call(c))}})},t(function(){var e;t.fn.qtip.cache={screen:{scroll:{left:t(window).scrollLeft(),top:t(window).scrollTop()},width:t(window).width(),height:t(window).height()}},t(window).on("resize scroll",function(o){clearTimeout(e),e=setTimeout(function(){"scroll"===o.type?t.fn.qtip.cache.screen.scroll={left:t(window).scrollLeft(),top:t(window).scrollTop()}:(t.fn.qtip.cache.screen.width=t(window).width(),t.fn.qtip.cache.screen.height=t(window).height());for(let i=0;i<t.fn.qtip.interfaces.length;i++){var e=t.fn.qtip.interfaces[i];!0===e.status.rendered&&("static"!==e.options.position.type||e.options.position.adjust.scroll&&"scroll"===o.type||e.options.position.adjust.resize&&"resize"===o.type)&&e.updatePosition(o,!0)}},100)}),t(document).on("mousedown.qtip",function(e){0===t(e.target).parents("div.qtip").length&&t(".qtip[unfocus]").each(function(){var o=t(this).qtip("api");t(this).is(":visible")&&!o.status.disabled&&t(e.target).add(o.elements.target).length>1&&o.hide(e)})})}),t.fn.qtip.interfaces=[],t.fn.qtip.log={error:function(){return this}},t.fn.qtip.constants={},t.fn.qtip.defaults={content:{prerender:!1,text:!1,url:!1,data:null,title:{text:!1,button:!1}},position:{target:!1,corner:{target:"bottomRight",tooltip:"topLeft"},adjust:{x:0,y:0,mouse:!0,screen:!1,scroll:!0,resize:!0},type:"absolute",container:!1},show:{when:{target:!1,event:"mouseover"},effect:{type:"fade",length:100},delay:140,solo:!1,ready:!1},hide:{when:{target:!1,event:"mouseout"},effect:{type:"fade",length:100},delay:0,fixed:!1},api:{beforeRender:function(){},onRender:function(){},beforePositionUpdate:function(){},onPositionUpdate:function(){},beforeShow:function(){},onShow:function(){},beforeHide:function(){},onHide:function(){},beforeContentUpdate:function(){},onContentUpdate:function(){},beforeContentLoad:function(){},onContentLoad:function(){},beforeTitleUpdate:function(){},onTitleUpdate:function(){},beforeDestroy:function(){},onDestroy:function(){},beforeFocus:function(){},onFocus:function(){}}},t.fn.qtip.styles={defaults:{background:"white",color:"#111",overflow:"hidden",textAlign:"left",width:{min:0,max:250},padding:"5px 9px",border:{width:1,radius:0,color:"#d3d3d3"},tip:{corner:!1,color:!1,size:{width:13,height:13},opacity:1},title:{background:"#e1e1e1",fontWeight:"bold",padding:"7px 12px"},button:{cursor:"pointer"},classes:{target:"",tip:"qtip-tip",title:"qtip-title",button:"qtip-button",content:"qtip-content",active:"qtip-active"}},cream:{border:{width:3,radius:0,color:"#F9E98E"},title:{background:"#F0DE7D",color:"#A27D35"},background:"#FBF7AA",color:"#A27D35",classes:{tooltip:"qtip-cream"}},light:{border:{width:3,radius:0,color:"#E2E2E2"},title:{background:"#f1f1f1",color:"#454545"},background:"white",color:"#454545",classes:{tooltip:"qtip-light"}},dark:{border:{width:3,radius:0,color:"#303030"},title:{background:"#404040",color:"#f3f3f3"},background:"#505050",color:"#f3f3f3",classes:{tooltip:"qtip-dark"}},red:{border:{width:3,radius:0,color:"#CE6F6F"},title:{background:"#f28279",color:"#9C2F2F"},background:"#F79992",color:"#9C2F2F",classes:{tooltip:"qtip-red"}},green:{border:{width:3,radius:0,color:"#A9DB66"},title:{background:"#b9db8c",color:"#58792E"},background:"#CDE6AC",color:"#58792E",classes:{tooltip:"qtip-green"}},blue:{border:{width:3,radius:0,color:"#ADD9ED"},title:{background:"#D0E9F5",color:"#5E99BD"},background:"#E5F6FE",color:"#4D9FBF",classes:{tooltip:"qtip-blue"}}}}(jQuery);
!function(){"use strict";class t{constructor(t,e,i){if(this.margins={top:0,right:0,bottom:0,left:0},!e.contentWindow)throw Error("Iframe argument must have been attached to DOM.");this.listener=i,this.iframe=e}setMessagePort(t){t.onmessage=t=>{this.onMessageFromIframe(t)}}show(){this.iframe.style.display="unset"}hide(){this.iframe.style.display="none"}setMargins(t){this.margins!=t&&(this.iframe.style.marginTop=this.margins.top+"px",this.iframe.style.marginLeft=this.margins.left+"px",this.iframe.style.marginBottom=this.margins.bottom+"px",this.iframe.style.marginRight=this.margins.right+"px")}loadPage(t){this.iframe.src=t}setPlaceholder(t){this.iframe.style.visibility="hidden",this.iframe.style.width=t.width+"px",this.iframe.style.height=t.height+"px",this.size=t}onMessageFromIframe(t){const e=t.data;switch(e.kind){case"contentSize":return this.onContentSizeAvailable(e.size);case"tap":return this.listener.onTap({x:e.x,y:e.y});case"linkActivated":return this.onLinkActivated(e)}}onLinkActivated(t){try{const e=new URL(t.href,this.iframe.src);this.listener.onLinkActivated(e.toString(),t.outerHtml)}catch(t){}}onContentSizeAvailable(t){t&&(this.iframe.style.width=t.width+"px",this.iframe.style.height=t.height+"px",this.size=t,this.listener.onIframeLoaded())}}class e{setInitialScale(t){return this.initialScale=t,this}setMinimumScale(t){return this.minimumScale=t,this}setWidth(t){return this.width=t,this}setHeight(t){return this.height=t,this}build(){const t=[];return this.initialScale&&t.push("initial-scale="+this.initialScale),this.minimumScale&&t.push("minimum-scale="+this.minimumScale),this.width&&t.push("width="+this.width),this.height&&t.push("height="+this.height),t.join(", ")}}class i{constructor(t,e){this.window=t,this.listener=e,document.addEventListener("click",(t=>{this.onClick(t)}),!1)}onClick(t){if(t.defaultPrevented)return;const e=this.window.getSelection();if(e&&"Range"==e.type)return;let i;i=t.target instanceof HTMLElement?this.nearestInteractiveElement(t.target):null,i?i instanceof HTMLAnchorElement&&this.listener.onLinkActivated(i.href,i.outerHTML):this.listener.onTap(t),t.stopPropagation(),t.preventDefault()}nearestInteractiveElement(t){return null==t?null:-1!=["a","audio","button","canvas","details","input","label","option","select","submit","textarea","video"].indexOf(t.nodeName.toLowerCase())||t.hasAttribute("contenteditable")&&"false"!=t.getAttribute("contenteditable").toLowerCase()?t:t.parentElement?this.nearestInteractiveElement(t.parentElement):null}}class s{constructor(e,s,n,a){this.fit="contain",this.insets={top:0,right:0,bottom:0,left:0},this.scale=1,e.addEventListener("message",(t=>{t.source===s.contentWindow&&t.ports[0]&&this.page.setMessagePort(t.ports[0])})),new i(e,{onTap:t=>{const e={x:(t.clientX-visualViewport.offsetLeft)*visualViewport.scale,y:(t.clientY-visualViewport.offsetTop)*visualViewport.scale};a.onTap(e)},onLinkActivated:t=>{throw Error("No interactive element in the root document.")}}),this.metaViewport=n;const o={onIframeLoaded:()=>{this.onIframeLoaded()},onTap:t=>{const e=s.getBoundingClientRect(),i={x:(t.x+e.left-visualViewport.offsetLeft)*visualViewport.scale,y:(t.y+e.top-visualViewport.offsetTop)*visualViewport.scale};a.onTap(i)},onLinkActivated:(t,e)=>{a.onLinkActivated(t,e)}};this.page=new t(e,s,o)}setViewport(t,e){this.viewport==t&&this.insets==e||(this.viewport=t,this.insets=e,this.layout())}setFit(t){this.fit!=t&&(this.fit=t,this.layout())}loadResource(t){this.page.hide(),this.page.loadPage(t)}onIframeLoaded(){this.page.size&&this.layout()}layout(){if(!this.page.size||!this.viewport)return;const t={top:this.insets.top,right:this.insets.right,bottom:this.insets.bottom,left:this.insets.left};this.page.setMargins(t);const i={width:this.viewport.width-this.insets.left-this.insets.right,height:this.viewport.height-this.insets.top-this.insets.bottom},s=function(t,e,i){switch(t){case"contain":return function(t,e){const i=e.width/t.width,s=e.height/t.height;return Math.min(i,s)}(e,i);case"width":return function(t,e){return e.width/t.width}(e,i);case"height":return function(t,e){return e.height/t.height}(e,i)}}(this.fit,this.page.size,i);this.metaViewport.content=(new e).setInitialScale(s).setMinimumScale(s).setWidth(this.page.size.width).setHeight(this.page.size.height).build(),this.scale=s,this.page.show()}}class n{constructor(t){this.nativeApi=t}onTap(t){this.nativeApi.onTap(JSON.stringify(t))}onLinkActivated(t,e){this.nativeApi.onLinkActivated(t,e)}}const a=document.getElementById("page"),o=document.querySelector("meta[name=viewport]");window.singleArea=new class{constructor(t,e,i,a){const o=new n(a);this.manager=new s(t,e,i,o)}loadResource(t){this.manager.loadResource(t)}setViewport(t,e,i,s,n,a){const o={width:t,height:e},h={top:i,left:a,bottom:n,right:s};this.manager.setViewport(o,h)}setFit(t){if("contain"!=t&&"width"!=t&&"height"!=t)throw Error(`Invalid fit value: ${t}`);this.manager.setFit(t)}}(window,a,o,window.gestures),window.initialization.onScriptsLoaded()}();
//# sourceMappingURL=fixed-single-script.js.map
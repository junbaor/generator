Ext.ux.IFrameComponent = Ext.extend(Ext.BoxComponent, {
    onRender : function(ct, position) {
        this.el = ct.createChild({tag: 'iframe', id: 'iframe-' + this.id,name:this.name, frameBorder: 0, src: this.url,width:'100%',height:'100%'});
    },
    setUrl: function(url) {
        this.el.dom.contentWindow.location.href = url;
    }
});
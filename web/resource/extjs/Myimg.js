Ext.form.Myimg = Ext.extend(Ext.BoxComponent, {
	//imagePath : "../../imgs/",
	onRender : function(ct, position) {
		if (!this.el&&this.value) {
			this.el = document.createElement('img');
			this.el.src = /*this.imagePath +*/ this.value;
			if (this.forId) {
				this.el.setAttribute('htmlFor', this.forId);
			}
		} else {
			if (this.forId) {
				this.el.setAttribute('htmlFor', this.forId);
			}
		}
		Ext.form.Label.superclass.onRender.call(this, ct, position);
	},
	setValue : function(v,flag) {
		this.value = v;
		if (!this.el||flag) {
			this.el = document.createElement('img');
			this.el.src =/* this.imagePath +*/ this.value;
			if (this.forId) {
				this.el.setAttribute('htmlFor', this.forId);
			}
		}else{
			this.el.src =/* this.imagePath +*/ this.value;
			if (this.forId) {
				this.el.setAttribute('htmlFor', this.forId);
			}
		}
		this.afterRender();
	},
	getValue : function() {
		return this.value;
	}
});
Ext.reg('myimg', Ext.form.Myimg);

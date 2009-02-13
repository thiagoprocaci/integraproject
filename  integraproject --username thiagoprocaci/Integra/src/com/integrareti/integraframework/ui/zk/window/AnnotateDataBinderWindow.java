package com.integrareti.integraframework.ui.zk.window;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.AnnotateDataBinder;

public abstract class AnnotateDataBinderWindow extends AbstractWindow {
	private Map<String, Object> bindObjects = new HashMap<String, Object>();
	private AnnotateDataBinder binder;

	/**
	 * This is declared final to make sure all sub-classes will AnnotateDataBind
	 * well. To do any action use <code>doOnCreate()</code>.
	 * 
	 * @see tagamaframework.window.AnnotateDataBinderWindow#doOnCreate()
	 * 
	 */
	public final void onCreate() {
		doOnCreate();
		bindComponents();
	}

	/**
	 * Any operations to be done in the creation fase. Similiar to default
	 * <code>onCreate()</code> of <code>org.zkoss.zul.Window</code>.
	 * 
	 * Also, any operations to be done before de binding action. Note that
	 * binding action will call the <code>getBindObject()</code> so this is a
	 * good place to call <code>setBindObject()</code> and set the object
	 * witch is going to be binded.
	 * 
	 * @see tagamaframework.window.AnnotateDataBinderWindow#doAfterBind()
	 */
	public void doOnCreate() {
	}

	/**
	 * Binds the window's components
	 */
	public final void bindComponents() {
		doBeforeBind();
		binder = new AnnotateDataBinder(this);
		Iterator<String> itKeys = bindObjects.keySet().iterator();
		Iterator<Object> itValues = bindObjects.values().iterator();
		// default bind name: "object"
		while (itKeys.hasNext()) {
			setVariable((String) itKeys.next(), itValues.next(), true);
		}
		// binder load
		getBinder().loadAll();
		doAfterBind();
	}

	/**
	 * Updates a bound window's component
	 * 
	 * @param component
	 */
	public final void updateBoundComponent(Component component) {
		doBeforeBind();
		Iterator<String> itKeys = bindObjects.keySet().iterator();
		Iterator<Object> itValues = bindObjects.values().iterator();
		// default bind name: "object"
		while (itKeys.hasNext())
			setVariable((String) itKeys.next(), itValues.next(), true);
		// binder load
		getBinder().loadComponent(component);
		doAfterBind();
	}

	/**
	 * Updates the bounds window's components
	 * 
	 * @param component
	 */
	public final void updateBoundComponents(List<Component> components) {
		doBeforeBind();
		Iterator<String> itKeys = bindObjects.keySet().iterator();
		Iterator<Object> itValues = bindObjects.values().iterator();
		// default bind name: "object"
		while (itKeys.hasNext())
			setVariable((String) itKeys.next(), itValues.next(), true);
		// binder load
		for (Component component : components)
			getBinder().loadComponent(component);
		doAfterBind();
	}

	/**
	 * Any operations to be done after the binding action.
	 * 
	 * For example call some code to select the first Listitem of a binded
	 * Listbox, avoid operations tha could result in NullPointerException.
	 * 
	 * @see tagamaframework.window.AnnotateDataBinderWindow#doBeforeBind()
	 */
	public void doAfterBind() {
		// implement in subclasses
	}

	/**
	 * 
	 * @return Returns binder
	 */
	public AnnotateDataBinder getBinder() {
		return binder;
	}

	/**
	 * Sets binder
	 * 
	 * @param binder
	 */
	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	/**
	 * 
	 * @return Returns bindObjects
	 */
	public Map<String, Object> getBindObjects() {
		return bindObjects;
	}

	/**
	 * Sets bindObjects
	 * 
	 * @param bindObjects
	 */
	public void setBindObjects(Map<String, Object> bindObjects) {
		this.bindObjects = bindObjects;
	}

	/**
	 * Any operations to be done before the binding action
	 */
	public abstract void doBeforeBind();
}

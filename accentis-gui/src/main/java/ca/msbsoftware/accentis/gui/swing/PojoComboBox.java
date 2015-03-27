package ca.msbsoftware.accentis.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComboBox;

import ca.msbsoftware.accentis.persistence.pojos.NamedObject;
import ca.msbsoftware.accentis.gui.GUIApplication;
import ca.msbsoftware.accentis.gui.models.PojoComboBoxModel;

@SuppressWarnings("serial")
public class PojoComboBox<T extends NamedObject> extends JComboBox<T> {

	private Constructor<T> constructor;
	
	public PojoComboBox(PojoComboBoxModel<T> model, Class<T> klass) {
		super(model);
		
		constructor = determineConstructor(klass);
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				assert null != command : "null action command encountered in payeeComboBox action listener."; //$NON-NLS-1$
				
				if (command.equals("comboBoxEdited")) { //$NON-NLS-1$
					Object selectedItem = getSelectedItem();
					if (selectedItem instanceof String) {
						String pojoName = (String) selectedItem;
						
						if (null == ((PojoComboBoxModel<T>) getModel()).lookupByName(pojoName)) {
							T pojo = createPojo();
							pojo.setName(pojoName);

							GUIApplication.getInstance().getPersistenceManager().create(pojo);
							setSelectedItem(pojo);
						}
					}
				}
			}
		});
		setEditable(true);
	}
	
	protected Constructor<T> determineConstructor(Class<T> klass) {
		try {
			return klass.getConstructor(new Class<?>[0]);
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
		} catch (SecurityException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	private T createPojo() {
		try {
			return constructor.newInstance(new Object[0]);
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
}

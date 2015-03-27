package ca.msbsoftware.accentis.gui.views;

import java.util.Date;

import ca.msbsoftware.accentis.persistence.pojos.Individual;

@SuppressWarnings("serial")
public class IndividualTableModel extends BasicTableModel<Individual> {

	private static final String[] COLUMN_NAMES = { "name", "birthdate", "spouse" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private static final Class<?>[] COLUMN_CLASSES = { String.class, Date.class, Individual.class };

	@Override
	protected String[] getColumnNames() {
		return COLUMN_NAMES;
	}

	@Override
	protected String getResourcePrefix() {
		return "individual"; //$NON-NLS-1$
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return COLUMN_CLASSES[column];
	}

	@Override
	protected Object getValue(Individual object, int column) {
		switch (column) {
		case 0:
			return object.getName();
		case 1:
			return object.getBirthDate();
		case 2:
			return object.getSpouse();
		}

		return null;
	}

	@Override
	protected String getQueryName() {
		return Individual.GET_ALL_INDIVIDUALS_QUERY;
	}

	@Override
	protected Class<Individual> getPojoClass() {
		return Individual.class;
	}

}

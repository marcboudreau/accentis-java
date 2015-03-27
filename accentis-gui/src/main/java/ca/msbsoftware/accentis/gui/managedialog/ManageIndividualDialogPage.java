package ca.msbsoftware.accentis.gui.managedialog;

import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.NORTH;
import static java.awt.GridBagConstraints.NORTHWEST;
import static java.awt.GridBagConstraints.WEST;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.msbsoftware.accentis.persistence.PersistenceManager;
import ca.msbsoftware.accentis.persistence.pojos.Individual;
import ca.msbsoftware.accentis.gui.ImageLoader;
import ca.msbsoftware.accentis.gui.InsetsConstants;
import ca.msbsoftware.accentis.gui.Resources;
import ca.msbsoftware.accentis.gui.models.PojoComboBoxModel;
import ca.msbsoftware.accentis.gui.swing.DateFormattedTextField;
import ca.msbsoftware.accentis.gui.swing.SINFormattedTextField;

public class ManageIndividualDialogPage extends AbstractManageDialogPage<Individual> {

	private static final Icon NO_PICTURE_ICON = ImageLoader.getIcon("nopicture"); //$NON-NLS-1$

	private static final Icon CANT_LOAD_PICTURE = ImageLoader.getIcon("cantloadpicture"); //$NON-NLS-1$

	private JComponent component;

	private JTextField nameField;

	private DateFormattedTextField dateBirthField;

	private SINFormattedTextField sinField;

	private JComboBox<Individual> spouseComboBox;

	private JButton imageButton;

	private JButton noImageButton;

	private byte[] imageBytes;

	private JFileChooser fileChooser;

	public ManageIndividualDialogPage(ManagePojoDialog dialog) {
		super(dialog);
	}

	JComponent getComponent() {
		if (null == component)
			createComponent();

		return component;
	}

	private void createComponent() {
		component = new JPanel(new GridBagLayout());

		component.add(new JLabel(Resources.getInstance().getString("manageindividualdialog.name.caption")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.ZERO_INSETS, 0, 0));
		component.add(getNameField(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageindividualdialog.image.caption")), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, //$NON-NLS-1$
				NORTHWEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getImageButton(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, CENTER, NONE, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));
		component.add(getNoImageButton(), new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0, WEST, NONE, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageindividualdialog.datebirth.caption")), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, //$NON-NLS-1$
				WEST, NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getDateBirthField(), new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageindividualdialog.sin.caption")), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, WEST, //$NON-NLS-1$
				NONE, InsetsConstants.FIVE_ON_TOP_INSETS, 0, 0));
		component.add(getSINField(), new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, CENTER, HORIZONTAL, InsetsConstants.FIVE_ON_TOP_AND_LEFT_INSETS, 0, 0));

		component.add(new JLabel(Resources.getInstance().getString("manageindividualdialog.spouse.caption")), new GridBagConstraints(0, 4, 1, 1, 0.0, 1.0, //$NON-NLS-1$
				NORTHWEST, NONE, InsetsConstants.FIVE_ON_TOP_AND_BOTTOM_INSETS, 0, 0));
		component.add(getSpouseComboBox(), new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0, NORTH, HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));

	}

	protected JTextField getNameField() {
		if (null == nameField)
			createNameField();

		return nameField;
	}

	private void createNameField() {
		nameField = new JTextField(20);
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				handleEvent();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				handleEvent();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				handleEvent();
			}

			private void handleEvent() {
				getManagePojoDialog().updateButtons();
			}
		});
	}

	protected DateFormattedTextField getDateBirthField() {
		if (null == dateBirthField)
			createDateBirthField();

		return dateBirthField;
	}

	private void createDateBirthField() {
		dateBirthField = new DateFormattedTextField();
	}

	protected JButton getImageButton() {
		if (null == imageButton)
			createImageButton();

		return imageButton;
	}

	private void createImageButton() {
		imageButton = new JButton(NO_PICTURE_ICON);
		imageButton.setMargin(new Insets(2, 2, 2, 2));
		imageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = getFileChooser();
				if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(getManagePojoDialog())) {
					File file = fileChooser.getSelectedFile();
					try {
						byte[] bytes = readBytes(file);
						Icon icon = buildIcon(bytes);
						if (null != icon) {
							imageButton.setIcon(icon);
							imageBytes = bytes;
						} else {
							JOptionPane.showMessageDialog(getManagePojoDialog(),
									Resources.getInstance().getString("message.manageindividualsdialog.pictureloadfail")); //$NON-NLS-1$
							imageButton.setIcon(CANT_LOAD_PICTURE);
							imageBytes = null;
						}
					} catch (IOException ex) {

					}
				}
			}

			private byte[] readBytes(File file) throws IOException {
				byte[] bytes = new byte[(int) file.length()];
				FileInputStream is = new FileInputStream(file);
				int read = 0;
				int total = 0;

				while (0 <= read && total < bytes.length) {
					read = is.read(bytes, total, bytes.length - total);
					total += read;
				}

				is.close();
				return bytes;
			}
		});
	}

	private static Icon buildIcon(byte[] imageData) {
		ImageIcon image = new ImageIcon(imageData);
		if (MediaTracker.COMPLETE == image.getImageLoadStatus())
			return scaledImage(image, 50, 50);

		return null;
	}

	private static ImageIcon scaledImage(ImageIcon imageIcon, int width, int height) {
		Image image = imageIcon.getImage();

		return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}

	private JFileChooser getFileChooser() {
		if (null == fileChooser)
			createFileChooser();

		return fileChooser;
	}

	private void createFileChooser() {
		fileChooser = new JFileChooser();
	}

	protected JButton getNoImageButton() {
		if (null == noImageButton)
			createNoImageButton();

		return noImageButton;
	}

	private void createNoImageButton() {
		noImageButton = new JButton(Resources.getInstance().getString("button.clear")); //$NON-NLS-1$
		noImageButton.setMargin(new Insets(2, 2, 2, 2));
		noImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				imageBytes = null;
				getImageButton().setIcon(NO_PICTURE_ICON);
			}
		});
	}

	protected SINFormattedTextField getSINField() {
		if (null == sinField)
			createSINField();

		return sinField;
	}

	private void createSINField() {
		sinField = new SINFormattedTextField();
	}

	protected JComboBox<Individual> getSpouseComboBox() {
		if (null == spouseComboBox)
			createSpouseComboBox();

		return spouseComboBox;
	}

	private void createSpouseComboBox() {
		spouseComboBox = new JComboBox<Individual>(new PojoComboBoxModel<>(Individual.GET_FILTERED_INDIVIDUALS_QUERY, Individual.class, true));
	}

	@Override
	protected void doFinish() {
		boolean existing = true;
		Individual individual = getCurrentPojo();

		if (null == individual) {
			individual = new Individual();
			existing = false;
		}

		individual.setName(getNameField().getText());
		individual.setBirthDate(getDateBirthField().getDateValue());
		individual.setSIN(getSINField().getSINValue());
		individual.setSpouse((Individual) getSpouseComboBox().getSelectedItem());
		individual.setPicture(getPictureImage());

		if (existing)
			getPersistenceManager().save(individual);
		else
			getPersistenceManager().create(individual);
	}

	private ca.msbsoftware.accentis.persistence.pojos.Image getPictureImage() {
		if (null == imageBytes)
			return null;

		ca.msbsoftware.accentis.persistence.pojos.Image image = new ca.msbsoftware.accentis.persistence.pojos.Image();
		image.setData(imageBytes);

		return image;
	}

	@Override
	boolean canFinish() {
		return !getNameField().getText().isEmpty();
	}

	@Override
	protected void updateFields() {
		PojoComboBoxModel<Individual> model = ((PojoComboBoxModel<Individual>) getSpouseComboBox().getModel());
		model.reload(PersistenceManager.createQueryParameterMap("excludedIndividual", getCurrentPojo())); //$NON-NLS-1$
		
		getNameField().setText(getCurrentPojo().getName());
		getDateBirthField().setValue(getCurrentPojo().getBirthDate());
		getSINField().setText(getCurrentPojo().getSIN().toString());
		getSpouseComboBox().setSelectedItem(getCurrentPojo().getSpouse());
		ca.msbsoftware.accentis.persistence.pojos.Image image = getCurrentPojo().getPicture();
		if (null != image)
			getImageButton().setIcon(buildIcon(image.getData()));
		else
			getImageButton().setIcon(NO_PICTURE_ICON);
	}

	@Override
	protected void clearFields() {
		getNameField().setText(null);
		getDateBirthField().setValue(new Date());
		getSINField().setText(null);
		getSpouseComboBox().setSelectedItem(null);
		getImageButton().setIcon(NO_PICTURE_ICON);
	}

	@Override
	String getTitleParameter() {
		return Resources.getInstance().getString("managepojodialog.individual.titleparameter"); //$NON-NLS-1$
	}
}

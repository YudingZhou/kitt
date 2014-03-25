package org.quantumlabs.kitt.ui.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.quantumlabs.kitt.Activator;
import org.quantumlabs.kitt.core.config.KITTParameter;
import org.quantumlabs.kitt.core.util.trace.ILogger;
import org.quantumlabs.kitt.core.util.trace.LogLevel;
import org.quantumlabs.kitt.core.util.trace.Logger;
import org.eclipse.swt.widgets.Label;

public class KITTMainPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private Button btnSystem;
	private Button btnError;
	private Button btnWarning;
	private Button btnDebug;
	private Button btnTrace;
	private Button btnNone;
	private Button btnBetaEnable;
	private Button btnBetaDisable;
	private Group grpRuntimeLogger;
	private Button btnConsoleLoggerEnable;
	private Button btnConsoleLoggerDisable;

	/**
	 * Create the preference page.
	 */
	public KITTMainPreferencePage() {
		setTitle("KITT");
	}
	@Override
	public Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));
		
		Group grpDevelopmentSetting = new Group(container, SWT.NONE);
		grpDevelopmentSetting.setText("Development setting");
		grpDevelopmentSetting.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpDevelopmentSetting.setLayout(new GridLayout(3, false));
		
		Group grpLogLevel = new Group(grpDevelopmentSetting, SWT.NONE);
		grpLogLevel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpLogLevel.setText("Log level");
		grpLogLevel.setLayout(new GridLayout(1, false));
		
		btnNone = new Button(grpLogLevel, SWT.RADIO);
		btnNone.setText("None");
		
		btnError = new Button(grpLogLevel, SWT.RADIO);
		btnError.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnError.setText("Error");
		
		btnWarning = new Button(grpLogLevel, SWT.RADIO);
		btnWarning.setText("Warning");
		
		btnDebug = new Button(grpLogLevel, SWT.RADIO);
		btnDebug.setText("Debug");
		
		btnTrace = new Button(grpLogLevel, SWT.RADIO);
		btnTrace.setText("Trace");
		
		btnSystem = new Button(grpLogLevel, SWT.RADIO);
		btnSystem.setText("System");
		
		Group grpBetaModel = new Group(grpDevelopmentSetting, SWT.NONE);
		grpBetaModel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpBetaModel.setText("Beta model");
		grpBetaModel.setLayout(new GridLayout(1, false));
		
		btnBetaEnable = new Button(grpBetaModel, SWT.RADIO);
		btnBetaEnable.setText("Enable");
		
		btnBetaDisable = new Button(grpBetaModel, SWT.RADIO);
		btnBetaDisable.setText("Disable");
		
		grpRuntimeLogger = new Group(grpDevelopmentSetting, SWT.NONE);
		grpRuntimeLogger.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		grpRuntimeLogger.setText("Console logger");
		grpRuntimeLogger.setLayout(new GridLayout(1, false));
		
		btnConsoleLoggerEnable = new Button(grpRuntimeLogger, SWT.RADIO);
		btnConsoleLoggerEnable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnConsoleLoggerEnable.setText("Enable");
		
		btnConsoleLoggerDisable = new Button(grpRuntimeLogger, SWT.RADIO);
		btnConsoleLoggerDisable.setText("Disable");
		

		return container;
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}
	
	private void intializeValues() {
		switch(KITTParameter.getLogLevel())
		{
		case 1://error
			btnError.setSelection(true);
			break;
		case 2://warning
			btnSystem.setSelection(true);
			break;
		case 3://debug
			btnDebug.setSelection(true);
			break;
		case 4://trace
			btnTrace.setSelection(true);
			break;
		case 5://system
			btnSystem.setSelection(true);
			break;
			default:
				btnNone.setSelection(true);
		}
		
		if(KITTParameter.isBETA()){
			btnBetaEnable.setSelection(true);
		}else{
			btnBetaDisable.setSelection(true);
		}
	}

	private void storeValues() {
		int logLevel = 0;
		if (btnNone.getSelection()) {
			logLevel = 0;
		} else if (btnError.getSelection()) {
			logLevel = LogLevel.ERROR.id();
		} else if (btnWarning.getSelection()) {
			logLevel = LogLevel.WARNING.id();
		} else if (btnDebug.getSelection()) {
			logLevel = LogLevel.DEBUG.id();
		} else if (btnTrace.getSelection()) {
			logLevel = LogLevel.TRACE.id();
		} else if (btnSystem.getSelection()) {
			logLevel = LogLevel.SYSTEM.id();
		}
		KITTParameter.setLogLevel(logLevel);
		
		boolean betaModel = false;
		if (btnBetaDisable.getSelection()) {
			betaModel = false;
		} else if (btnBetaEnable.getSelection()) {
			betaModel = true;
		}
		KITTParameter.setBETA(betaModel);
		
		if(btnConsoleLoggerEnable.getSelection()){
			Logger.setLogger(ILogger.CONSOLE_LOGGER);
		}else if(btnConsoleLoggerDisable.getSelection()){
			Logger.setLogger(ILogger.DEFAULT_LOGGER);
		}
	}

	private void initializeDefaults() {
		btnNone.setSelection(true);
		btnBetaDisable.setSelection(true);
	}

	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	@Override
	public boolean performCancel() {
		intializeValues();
		return super.performCancel();
	}

	@Override
	protected void performDefaults() {
		initializeDefaults();
		super.performDefaults();
	}

	@Override
	public boolean performOk() {
		storeValues();
		return super.performOk();
	}
}

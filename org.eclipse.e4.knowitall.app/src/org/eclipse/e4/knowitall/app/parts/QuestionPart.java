package org.eclipse.e4.knowitall.app.parts;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.inject.Inject;

import knowitallservice.IKnowItAll;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.knowitall.special.annotations.Special;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class QuestionPart extends Composite implements SelectionListener {
	private static final boolean mocking = false;
	private Text questionText;
	private Text answerText;
	
//	private final IKnowItAll knowItAll;
	private IEclipseContext context;
private IEventBroker eventBroker;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	@Inject
	public QuestionPart(Composite parent, IEclipseContext context, IEventBroker eventBroker) {
		super(parent, SWT.NONE);
//		this.knowItAll = knowItAll;
		this.setContext(context);
		this.setEventBroker(eventBroker);
		questionText = new Text(this, SWT.BORDER);
		questionText.setBounds(10, 38, 266, 21);

		Button btnFindOut = new Button(this, SWT.NONE);
		btnFindOut.addSelectionListener(this);
		btnFindOut.setBounds(282, 38, 75, 25);
		btnFindOut.setText("Find out!");

		answerText = new Text(this, SWT.BORDER | SWT.MULTI);
		answerText.setEditable(false);
		answerText.setBounds(10, 64, 348, 175);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		widgetDefaultSelected(e);

	}
	
	

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		if (mocking) {
			IKnowItAll knowItAll = mock(IKnowItAll.class);
			when(knowItAll.answer("What?")).thenReturn("Ca bone");
			when(knowItAll.answer("When?")).thenReturn("Ja ime");
			when(knowItAll.answer("How?")).thenReturn("Shpejt e shpejt");

			answerText.append(knowItAll.answer(questionText.getText()) + "\n");
		} else {
			
			context.set("question", questionText.getText());
			IKnowItAll knowItAll = context.get(IKnowItAll.class);
			answerText.append(knowItAll.answer(questionText.getText()) + "\n");
			questionText.setText("");
			questionText.forceFocus();
			eventBroker.post("SPECIAL_EVENT","Helloui");
		;}
	}

	@Inject
	private void specialEvent(@Optional @UIEventTopic("SPECIAL_EVENT") String dummy, MPart part, @Special String special){
		if (dummy==null) return;
//		else System.err.println("TAMAM");
		part.setLabel(special);
	}
	
	public IEclipseContext getContext() {
		return context;
	}

	public void setContext(IEclipseContext context) {
		this.context = context;
	}

	public IEventBroker getEventBroker() {
		return eventBroker;
	}

	public void setEventBroker(IEventBroker eventBroker) {
		this.eventBroker = eventBroker;
	}
}

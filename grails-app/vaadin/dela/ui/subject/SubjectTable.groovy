package dela.ui.subject

import com.vaadin.data.Item
import com.vaadin.ui.Button
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.CheckBox
import com.vaadin.ui.Component
import com.vaadin.ui.ComponentContainer
import com.vaadin.ui.Field
import com.vaadin.ui.FormFieldFactory
import com.vaadin.ui.TextField

import dela.YesNoDialog
import dela.ui.common.EntityForm
import dela.ui.common.EntityTable
import com.vaadin.data.Container
import dela.VaadinService

import dela.SubjectService
import dela.IDataService
import dela.context.DataContext
import dela.TaskService
import dela.MessageService

/**
 * @author vedi
 * date 08.07.2010
 * time 22:33:52
 */
class SubjectTable extends EntityTable implements FormFieldFactory {

    VaadinService vaadinService
    def taskService
    MessageService messageService
    def accountService

    def gridVisibleColumns = ['name']
    def formFieldFactory = this
    def normalizeButton

    def SubjectTable() {
        this.vaadinService = getBean(VaadinService.class)
        this.taskService = getBean(TaskService.class)
        this.messageService = getBean(MessageService.class)
    }

    protected IDataService initDataService() {
        return getBean(SubjectService.class)
    }


    protected Container createContainer(DataContext dataContext) {
        return vaadinService.createSubjectDefaultContainer(dataContext)
    }

    Field createField(Item item, Object propertyId, Component component) {
        String label = getColumnLabel(propertyId)
        if ('isPublic'.equals(propertyId)) {
            def checkBox = new CheckBox(label)
            checkBox.readOnly = !this.dataContext.account.isAdmin()
            checkBox
        } else {
            TextField textField = new TextField(label)
            textField.setNullRepresentation('')

            if ('description'.equals(propertyId)) {
                textField.setRows(10)
                textField.setColumns(30)
            }

            textField
        }

    }

    def afterInsert(item) {

        super.afterInsert(item)

        addWindow(new YesNoDialog(
                this.messageService.getSetSubjectActiveConfirmCaption(),
                this.messageService.getSetSubjectActiveConfirmMsg(),
                this.messageService.getYesButtonLabel(),
                this.messageService.getNoButtonLabel(),
                new YesNoDialog.Callback() {
                    public void onDialogResult(boolean yes) {
                        if (yes) {
                            def dataContext = SubjectTable.this.dataContext
                            def subject = getDomain(item)
                            assert subject, getDomain(item).errors()
                            def setup = dataContext.setup
                            setup.setActiveSubject(subject)
                            dataContext.storeService.saveSetup(setup)
                        }
                    }

                }))
    }

    protected EntityForm createForm() {
        return new SubjectForm()
    }

    class SubjectForm extends EntityForm {

        protected void initButtons(ComponentContainer componentContainer) {
            super.initButtons(componentContainer);

            if (editable) {
                normalizeButton = new Button()
                normalizeButton.caption = messageService.getNormalizeButtonLabel()
                normalizeButton.addListener(this as Button.ClickListener)
                componentContainer.addComponent(normalizeButton)
            }
        }

        def void buttonClick(ClickEvent clickEvent) {
            if (clickEvent.button == normalizeButton) {
                taskService.normalizeSubjectTasks(getDomain(getItemDataSource()))
                window.application.mainWindow.showNotification(messageService.getNormalizeIsCompletedMsg())
            } else {
                super.buttonClick(clickEvent)
            }
        }
    }

}

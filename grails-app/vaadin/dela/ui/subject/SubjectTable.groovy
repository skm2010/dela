package dela.ui.subject

import dela.ui.common.EntityForm
import dela.ui.common.EntityTable
import dela.*
import com.vaadin.data.util.BeanItem

/**
 * @author vedi
 * date 08.07.2010
 * time 22:33:52
 */
class SubjectTable extends EntityTable {

    def gridFields = ['name']

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

    protected def createForm() {
        return new SubjectForm()
    }

    @Override
    protected def toFormItem(item) {
        assert item
        assert item.bean instanceof Subject

        def subjectCommand = null

        Subject.withTransaction {
            def subject = item.bean.id ? item.bean.merge() : item.bean
            assert subject
            subjectCommand = new SubjectCommand(subject)
            return new BeanItem(subjectCommand)
        }
    }

    @Override
    protected fromFormItem(Object item) {
        assert item
        assert item.bean instanceof SubjectCommand

        Subject.withTransaction {
            return new BeanItem(((SubjectCommand)item.bean).getSubject())
        }
    }

}

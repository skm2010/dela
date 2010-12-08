package dela

import dela.context.DataContext
import com.vaadin.data.Validator.InvalidValueException

/**
 * canInsert, canEdit - is action available in current context, runs twice or more times in UI and in logic
 * canSave - can the domain be persisted, for edit action initial state gives
 * domain's validate - last validation 
 */
class DataService<T> implements IDataService<T> {

    def create(DataContext dataContext) {
        return dataContext.domainClass.newInstance()
    }

    def save(DataContext dataContext, T domain) {

        boolean isNew = domain.id == null

        def gainedDomain = isNew?null:gainDomain(dataContext, domain)

        if (isNew) {
            assert canInsert(dataContext)
        } else {
            assert canEdit(dataContext, gainedDomain)
        }

        // TODO: Translate to VAADIN validation
        assert canSave(dataContext, gainedDomain, domain)

        if (isNew) {
            assert domain.save()
            afterInsert(dataContext, domain)
        } else {
            assert domain.validate()
            domain = domain.merge()
            afterEdit(dataContext, domain)
        }

        return domain
    }

    def delete(DataContext dataContext, T domain) {
        assert canDelete(dataContext, gainDomain(dataContext, domain))
        domain.merge().delete()
        afterDelete(dataContext, domain)
    }

    def afterInsert(DataContext dataContext, T domain) {
    }

    def afterEdit(DataContext dataContext, T domain) {
    }

    def afterDelete(DataContext dataContext, T domain) {
    }

    def Boolean canInsert(DataContext dataContext) {
        return true
    }

    def Boolean canEdit(DataContext dataContext, T domain) {
        return true
    }

    def Boolean canDelete(DataContext dataContext, T domain) {
        return canEdit(dataContext, domain)
    }

    def Boolean canSave(DataContext dataContext, T oldDomain, T newDomain) {
        return true
    }

    protected T gainDomain(DataContext dataContext, T domain) {
        dataContext.domainClass.get(domain.id) //TODO: Test the same
    }
}

package dela.meta

import dela.State
import dela.Subject
import dela.Task
import dela.Version

/**
 * @author vedi
 * date 28.06.2010
 * time 15:13:41
 */
class MetaProvider {

    def storeService

    public final MetaDomain taskMeta = new MetaDomain(
            domainClass: Task.class,
            caption: "Tasks",
            columns: [
                    new MetaColumn(field: 'id', type:Long.class, readOnly: true),
                    new MetaColumn(field: 'name', readOnly: true),
                    new MetaColumn(field: 'description'),
                    new MetaColumn(field: 'power', type:Double.class, ),
                    new MetaColumn(field: 'subject', type:Subject.class),
                    new MetaColumn(field: 'state', type:State.class),
                    new MetaColumn(field: 'subjectVersion',type:Version.class, label: 'version'),
                    new MetaColumn(field: 'dateCreated', type:Date.class, label:'creation date'),
            ],
    )

    public final MetaDomain stateMeta = new MetaDomain(domainClass: State.class,
            caption: "States",
            columns: [
                    new MetaColumn(field: 'name', readOnly: true, sortable: true),
            ],
    )

    public final MetaDomain subjectMeta = new MetaDomain(
            domainClass: Subject.class,
            caption: "Subjects",
            columns: [
                    new MetaColumn(field: 'name', readOnly: true, sortable: true),
                    new MetaColumn(field: 'description'),
            ],
    )
}

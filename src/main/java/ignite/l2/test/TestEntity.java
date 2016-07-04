package ignite.l2.test;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "test_entity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "count", nullable = false)
    private int count = 0;

    public TestEntity(String id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    TestEntity() {
        // Required by Hibernate
    }

    public int increment() {
        return ++count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestEntity)) return false;

        TestEntity that = (TestEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "TestEntity{" + "id='" + id + '\'' + ", count=" + count + '}';
    }
}

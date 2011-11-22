package org.openhds.domain.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.openhds.domain.annotations.Description;
import org.openhds.domain.constraint.Searchable;

/**
 * @author Dave Roberge
 */
@Description(description="A Role represents the different actors who use the system. " +
		"A Role contains a group of Privileges. They are assigned to Users which define " +
		"the actions they can take within OpenHDS.")
@Entity
@Table(name="role")
public class Role extends AuditableEntity implements Serializable {

    static final long serialVersionUID = 21L;

    @Searchable
    @Description(description="Name of the role.")
    String name;

    @Description(description="Description of the role.")
    String description;

    @ManyToMany(fetch=FetchType.EAGER)
    @Description(description="Set of privileges which define the rights that actors have.")
    @JoinTable(name="role_privileges", joinColumns = {@JoinColumn(name="role_uuid")}, inverseJoinColumns = @JoinColumn(name="privilege_uuid"))
    Set<Privilege> privileges = new HashSet<Privilege>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role other = (Role) obj;
        if ((this.uuid == null) ? (other.uuid != null) : !this.uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.uuid != null ? this.uuid.hashCode() : 0);
        return hash;
    }



    @Override
    public String toString() {
        return name;
    }
}

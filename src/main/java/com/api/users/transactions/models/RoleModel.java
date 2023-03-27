package com.api.users.transactions.models;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import com.api.users.transactions.enums.RoleName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_ROLE")
public class RoleModel implements GrantedAuthority, Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID roleId;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private RoleName roleName;
	@Override
	public String getAuthority() {
		return this.roleName.toString();
	}

	public UUID getRoleId() {
		return roleId;
	}

	public void setRoleId(UUID roleId) {
		this.roleId = roleId;
	}

	public RoleName getRoleName() {
		return roleName;
	}

	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}

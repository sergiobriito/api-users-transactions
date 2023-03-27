package com.api.users.transactions.dtos;
	
import java.math.BigDecimal;
import java.util.List;

import com.api.users.transactions.enums.RoleName;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {

		@NotBlank
		private String username;
		@NotBlank
		private String name;
		@NotBlank
		@Pattern(regexp = "^(\\d{11}|\\d{14})$")
		private String cpf;
		@Email
		private String email;
		@NotBlank
		private String password;
		@Min(value = 0, message = "Balance cannot be negative")
	    private BigDecimal balance = BigDecimal.ZERO;
		@NotEmpty
		private List<RoleName> roles;
		
		
		public List<RoleName> getRoles() {
			return roles;
		}
		public void setRoles(List<RoleName> roles) {
			this.roles = roles;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCpf() {
			return cpf;
		}
		public void setCpf(String cpf) {
			this.cpf = cpf;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
		public BigDecimal getBalance() {
			return balance;
		}
		public void setBalance(BigDecimal balance) {
			this.balance = balance;
		}
		
		public boolean isValidCpfOrCnpj() {
	        return isValidCPF(this.cpf) || isValidCNPJ(this.cpf);
	    }
	
	    private boolean isValidCPF(String cpf) {
	        CPFValidator cpfValidator = new CPFValidator();
	        try {
	            cpfValidator.assertValid(cpf);
	            return true;
	        } catch (InvalidStateException  e) {
	            return false;
	        }
	    }
	
	    private boolean isValidCNPJ(String cnpj) {
	        CNPJValidator cnpjValidator = new CNPJValidator();
	        try {
	            cnpjValidator.assertValid(cnpj);
	            return true;
	        } catch (InvalidStateException  e) {
	            return false;
	        }
	    }
		
	
	}

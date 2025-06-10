package smartmunimji.backend.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pizza_customers")
public class Customer implements UserDetails {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="ID")
	private int id;
	@Column(name="Name")
	private String name;
	@Column(name="Email")
	private String email;
	@Column(name="Password")
	private String password;
	@Column(name="Mobile")
	private String mobile;
	@Column(name="Address")
	private String address;
	@Column(name="Role")
	private String role;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(this.role);
		return authorities;
	}
	@Override
	public String getUsername() {
		return this.email;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
}
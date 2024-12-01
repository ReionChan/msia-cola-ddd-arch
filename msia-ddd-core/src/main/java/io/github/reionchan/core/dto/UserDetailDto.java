package io.github.reionchan.core.dto;

import io.github.reionchan.core.consts.UserStatus;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

import static io.github.reionchan.commons.validation.CommonUtil.*;

/**
 * 用户详情，适配 SpringSecurity
 *
 * @author Reion
 * @date 2024-01-10
 **/
public class UserDetailDto implements UserDetails, CredentialsContainer {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String userName;
    private String password;
    private Byte status;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailDto(Long id, String username, String password, Byte status, Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(username != null && !"".equals(username),
                "Cannot pass null or empty values to constructor");
        this.id = id;
        this.userName = username;
        this.password = password;
        this.status = status;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ENABLED.getValue().equals(this.status);
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserDetailDto user) {
            return this.userName.equals(user.getUsername());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.userName.hashCode();
    }

    public static UserDetailDtoBuilder builder(UserDto userDto) {
        Assert.isTrue(isNotEmpty(userDto) && isNotEmpty(userDto.getUserName()) && isNotEmpty(userDto.getStatus())
        && isNotEmpty(userDto.getRoles()), "缺失必要信息");
        return new UserDetailDtoBuilder()
                .id(userDto.getId())
                .username(userDto.getUserName())
                .password(userDto.getPassword())
                .roles(userDto.getRoles().split(","))
                .status(UserStatus.of(userDto.getStatus()));
    }

    public static final class UserDetailDtoBuilder {

        private Long id;

        private String username;

        private String password;

        private List<GrantedAuthority> authorities = new ArrayList<>();

        private Byte status;

        private Function<String, String> passwordEncoder = (password) -> password;

        private UserDetailDtoBuilder() {
        }

        public UserDetailDtoBuilder id(Long id) {
            Assert.notNull(id, "id cannot be null");
            this.id = id;
            return this;
        }

        public UserDetailDtoBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public UserDetailDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDetailDtoBuilder passwordEncoder(Function<String, String> encoder) {
            Assert.notNull(encoder, "encoder cannot be null");
            this.passwordEncoder = encoder;
            return this;
        }

        public UserDetailDtoBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
            for (String role : roles) {
                Assert.isTrue(!role.startsWith("ROLE_"),
                        () -> role + " cannot start with ROLE_ (it is automatically added)");
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return authorities(authorities);
        }

        public UserDetailDtoBuilder authorities(GrantedAuthority... authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            return authorities(Arrays.asList(authorities));
        }

        public UserDetailDtoBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public UserDetailDtoBuilder authorities(String... authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            return authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public UserDetailDtoBuilder status(UserStatus userStatus) {
            this.status = userStatus.getValue();
            return this;
        }

        public UserDetails build() {
            String encodedPassword = this.passwordEncoder.apply(this.password);
            return new UserDetailDto(this.id, this.username, encodedPassword, this.status, this.authorities);
        }
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per
        // UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            }
            if (g1.getAuthority() == null) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }
}

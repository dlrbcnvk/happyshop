package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.entity.MemberRole;
import com.project.happyshop.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    private final JpaMemberRoleRepository jpaMemberRoleRepository;

    public void save(Member member) { em.persist(member); }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByEmailAndProvider(String email, SocialProvider provider) {
        return em.createQuery("select m from Member m " +
                "where m.email =: email " +
                "and m.provider =: provider")
                .setParameter("email", email)
                .setParameter("provider", provider)
                .getResultList();
    }

    public Set<Role> findRoles(Member member) {
        return member.getMemberRoles().stream()
                .map(MemberRole::getRole)
                .collect(Collectors.toUnmodifiableSet());
    }
}

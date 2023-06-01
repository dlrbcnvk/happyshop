package com.project.happyshop.service;

import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.entity.Item;
import com.project.happyshop.domain.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private MemberService memberService;

    @Test
    void 상품등록() {
        Item item = Item.createItem(
                "구름 쿠버네티스 과정",
                200000,
                50,
                "구름 구름 구름~",
                "https://picsum.photos/300/200");

        Member member = Member.createMember(
                "test111@naver.com",
                SocialProvider.LOCAL,
                "user222",
                "dkssudgktpdy!",
                "010-1111-2323",
                new Address("juso", "jusoDetail", "14555")
        );
        item.setSeller(member);

        memberService.join(member);

        itemService.save(item);
    }
}

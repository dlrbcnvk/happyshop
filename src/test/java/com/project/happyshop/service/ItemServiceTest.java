package com.project.happyshop.service;

import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.entity.Item;
import com.project.happyshop.domain.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

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

    @Test
    void 상품조회_판매하는상품제외하고() {
        Member member1 = Member.createMember(
                "test111@naver.com",
                SocialProvider.LOCAL,
                "user222",
                "dkssudgktpdy!",
                "010-1111-2323",
                new Address("juso", "jusoDetail", "14555")
        );
        Member member2 = Member.createMember(
                "test222@naver.com",
                SocialProvider.LOCAL,
                "user442134",
                "dkssudgktpdy!",
                "010-1111-2323",
                new Address("juso", "jusoDetail", "14555")
        );
        memberService.join(member1);
        memberService.join(member2);

        for (int i = 0; i < 10; i++) {
            Item item = Item.createItem(
                    "아이템" + i,
                    200000,
                    50,
                    "구름 구름 구름~",
                    "https://picsum.photos/300/200");
            item.setSeller(member1);
            itemService.save(item);
        }

        List<Item> itemListAll = itemService.findAll();
        List<Item> itemListNotMember1 = itemService.findBySellerNot(member1);
        List<Item> itemListNotMember2 = itemService.findBySellerNot(member2);

        assertThat(itemListAll.size()).isEqualTo(10);
        assertThat(itemListNotMember1.size()).isEqualTo(0);
        assertThat(itemListNotMember2.size()).isEqualTo(10);

    }
}

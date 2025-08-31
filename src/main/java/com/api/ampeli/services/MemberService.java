package com.api.ampeli.services;

import com.api.ampeli.model.Member;
import com.api.ampeli.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByUserId(Long userId) {
        return memberRepository.findByUserId(userId);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public List<Member> findByFaithStage(Member.FaithStage faithStage) {
        return memberRepository.findByFaithStage(faithStage);
    }

    public List<Member> findByInterestAreasContaining(String interest) {
        return memberRepository.findByInterestAreasContaining(interest);
    }

    public List<Member> findByVolunteerAreaContaining(String area) {
        return memberRepository.findByVolunteerAreaContaining(area);
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Member update(Long id, Member memberDetails) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));

        // Update all fields
        member.setFullName(memberDetails.getFullName());
        member.setBirthDate(memberDetails.getBirthDate());
        member.setGender(memberDetails.getGender());
        member.setMaritalStatus(memberDetails.getMaritalStatus());
        member.setEmail(memberDetails.getEmail());
        member.setPhone(memberDetails.getPhone());
        member.setChurchAttendanceTime(memberDetails.getChurchAttendanceTime());
        member.setPreviousChurches(memberDetails.getPreviousChurches());
        member.setHowFoundChurch(memberDetails.getHowFoundChurch());
        member.setPreviousParticipation(memberDetails.getPreviousParticipation());
        member.setInterestAreas(memberDetails.getInterestAreas());
        member.setSkillsGifts(memberDetails.getSkillsGifts());
        member.setVolunteerArea(memberDetails.getVolunteerArea());
        member.setAvailableDaysTimes(memberDetails.getAvailableDaysTimes());
        member.setEventPreference(memberDetails.getEventPreference());
        member.setInterestsIn(memberDetails.getInterestsIn());
        member.setChurchSearch(memberDetails.getChurchSearch());
        member.setOpenToNewGroups(memberDetails.getOpenToNewGroups());
        member.setGroupPreference(memberDetails.getGroupPreference());
        member.setFaithStage(memberDetails.getFaithStage());
        member.setPastoralSupportInterest(memberDetails.getPastoralSupportInterest());
        member.setFaithDifficulties(memberDetails.getFaithDifficulties());

        return memberRepository.save(member);
    }

    public void deleteById(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new RuntimeException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }
}

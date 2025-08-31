package com.api.ampeli.controller;

import com.api.ampeli.model.Member;
import com.api.ampeli.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAll();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberService.findById(id);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Member> getMemberByUserId(@PathVariable Long userId) {
        Optional<Member> member = memberService.findByUserId(userId);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
        Optional<Member> member = memberService.findByEmail(email);
        return member.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/faith-stage/{faithStage}")
    public ResponseEntity<List<Member>> getMembersByFaithStage(@PathVariable String faithStage) {
        try {
            Member.FaithStage stage = Member.FaithStage.valueOf(faithStage.toUpperCase());
            List<Member> members = memberService.findByFaithStage(stage);
            return ResponseEntity.ok(members);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/interest/{interest}")
    public ResponseEntity<List<Member>> getMembersByInterest(@PathVariable String interest) {
        List<Member> members = memberService.findByInterestAreasContaining(interest);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/volunteer-area/{area}")
    public ResponseEntity<List<Member>> getMembersByVolunteerArea(@PathVariable String area) {
        List<Member> members = memberService.findByVolunteerAreaContaining(area);
        return ResponseEntity.ok(members);
    }

    @PostMapping
    public ResponseEntity<?> createMember(@Valid @RequestBody Member member) {
        try {
            Member savedMember = memberService.save(member);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @Valid @RequestBody Member memberDetails) {
        try {
            Member updatedMember = memberService.update(id, memberDetails);
            return ResponseEntity.ok(updatedMember);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

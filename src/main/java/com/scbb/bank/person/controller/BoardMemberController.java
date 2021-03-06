package com.scbb.bank.person.controller;

import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.person.model.BoardMember;
import com.scbb.bank.person.service.BoardMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/boardMembers")
public class BoardMemberController implements AbstractController<BoardMember, Integer> {

	private BoardMemberService boardMemberService;

	public BoardMemberController(BoardMemberService boardMemberService) {
		this.boardMemberService = boardMemberService;
	}

	@GetMapping
	public List<BoardMember> findAll() {
		return modifyResources(boardMemberService.findAll());
	}

	@GetMapping("/divisionIsNull")
	public ResponseEntity<List<BoardMember>> findAllByDivisionIsNull() {
		return ResponseEntity.ok(modifyResources(boardMemberService.findAllByDivisionIsNull()));
	}

	@GetMapping("{id}")
	public BoardMember findById(@PathVariable Integer id) {
		return modifyResource(boardMemberService.findById(id));
	}

	@PostMapping
	@PutMapping
	public BoardMember persist(@RequestBody BoardMember boardMember) {
		return modifyResource(boardMemberService.persist(boardMember));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		boardMemberService.delete(id);
		return ResponseEntity.ok("Successfully deleted board member with having id: " + id);
	}

	@PutMapping("search")
	public List<BoardMember> search(@RequestBody BoardMember boardMember) {
		return modifyResources(boardMemberService.search(boardMember));
	}

	@Override
	public BoardMember modifyResource(BoardMember boardMember) {
		if (boardMember.getUser() != null) {
			boardMember.getUser().setBoardMember(null);
			boardMember.getUser().setStaff(null);
			boardMember.getUser().setAuthorities(null);
		}
		if (boardMember.getMember() != null) {
			boardMember.getMember().setBoardMember(null);
			boardMember.getMember().setTeam(null);
			boardMember.getMember().setSubsidy(null);
			boardMember.getMember().setShareAccount(null);
			boardMember.getMember().setSavingsList(null);
			boardMember.getMember().setLoanList(null);
		}
		if (boardMember.getDivision() != null) {
			boardMember.getDivision().setBoardMember(null);
			boardMember.getDivision().setStaff(null);
			boardMember.getDivision().setSocietyList(null);
		}
		if (boardMember.getAttendanceList() != null) {
			boardMember.getAttendanceList().forEach(attendance -> {
				attendance.setBoardMember(null);
				attendance.setMeeting(null);
				attendance.setMeeting(null);
			});
		}
		return boardMember;
	}

	@Override
	public List<BoardMember> modifyResources(List<BoardMember> boardMemberList) {
		return boardMemberList
				.stream()
				.map(this::modifyResource)
				.collect(Collectors.toList());
	}
}

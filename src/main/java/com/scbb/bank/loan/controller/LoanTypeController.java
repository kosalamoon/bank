package com.scbb.bank.loan.controller;


import com.scbb.bank.interfaces.AbstractController;
import com.scbb.bank.loan.model.LoanType;
import com.scbb.bank.loan.service.LoanTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("loanTypes")
public class LoanTypeController implements AbstractController<LoanType, Integer> {

	private LoanTypeService loanTypeService;

	public LoanTypeController(LoanTypeService loanTypeService) {
		this.loanTypeService = loanTypeService;
	}


	@GetMapping
	public List<LoanType> findAll() {
		return loanTypeService.findAll();
	}

	@GetMapping("{id}")
	public LoanType findById(@PathVariable Integer id) {
		return loanTypeService.findById(id);
	}

	@PostMapping
	@PutMapping
	public LoanType persist(@RequestBody LoanType loanType) {
		return loanTypeService.persist(loanType);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {
		loanTypeService.delete(id);
		return ResponseEntity.ok("Successfully deleted loan type with having id: " + id);
	}

	@PutMapping("search")
	public List<LoanType> search(@RequestBody LoanType loanType) {
		return loanTypeService.search(loanType);
	}

	@Override
	public LoanType modifyResource(LoanType loanType) {
		return null;
	}

	@Override
	public List<LoanType> modifyResources(List<LoanType> loanTypes) {
		return null;
	}
}
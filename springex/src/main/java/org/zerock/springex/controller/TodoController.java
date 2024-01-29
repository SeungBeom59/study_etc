package org.zerock.springex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.springex.dto.PageRequestDTO;
import org.zerock.springex.dto.TodoDTO;
import org.zerock.springex.mapper.TodoMapper;
import org.zerock.springex.service.TodoService;

import javax.validation.Valid;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    // todo리스트
    @RequestMapping("/list")
    public void list(@Valid PageRequestDTO pageRequestDTO, BindingResult bindingResult, Model model){
        log.info("todo list.......");
        log.info(pageRequestDTO);

        if(bindingResult.hasErrors()){
            pageRequestDTO = PageRequestDTO.builder().build();
        }
        model.addAttribute("responseDTO" , todoService.getList(pageRequestDTO));
    }

    //todo등록
    //@RequestMapping(value = "/register" , method = RequestMethod.GET)
    @GetMapping("/register")
    public void register(){
        log.info("todo register.......");
    }

    // todo등록
    @PostMapping("/register")
    public String registerPost(@Valid TodoDTO todoDTO ,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes ){
        log.info("POST todo register.......");
        log.info("에러판별전::{}" , todoDTO);

        if(bindingResult.hasErrors()){
            log.info("has errors.....");
            redirectAttributes.addFlashAttribute("errors" , bindingResult.getAllErrors());
            return "redirect:/todo/register";
        }
        log.info(todoDTO);

        todoService.register(todoDTO);

        return "redirect:/todo/list";
    }

    // todo읽기,수정
    @GetMapping({"/read" , "/modify"})
    public void read(Long tno , PageRequestDTO pageRequestDTO, Model model){
        log.info("read 실행");
        TodoDTO todoDTO = todoService.getOne(tno);
        log.info(todoDTO);
        model.addAttribute("dto",todoDTO);
    }
    // todo삭제
    @PostMapping("/remove")
    public String remove(Long tno , RedirectAttributes redirectAttributes , PageRequestDTO pageRequestDTO){
        log.info("---------------remove-------------------");
        log.info("tno: " + tno);

        todoService.remove(tno);

        redirectAttributes.addAttribute("page", 1);
        redirectAttributes.addAttribute("size", pageRequestDTO.getSize());
        return "redirect:/todo/list?" + pageRequestDTO.getLink();
    }

    // todo수정(post)
    @PostMapping("/modify")
    public String modify(@Valid TodoDTO todoDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         PageRequestDTO pageRequestDTO){
        if(bindingResult.hasErrors()){
            log.info("has errors......");
            redirectAttributes.addFlashAttribute("errors" , bindingResult.getAllErrors());
            redirectAttributes.addAttribute("tno",todoDTO.getTno());
            return "redirect:/todo/modify";
        }
        log.info(todoDTO);
        todoService.modify(todoDTO);

//        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
//        redirectAttributes.addAttribute("size", pageRequestDTO.getSize());
        redirectAttributes.addAttribute("tno" , todoDTO.getTno());
        return "redirect:/todo/read";
    }



}

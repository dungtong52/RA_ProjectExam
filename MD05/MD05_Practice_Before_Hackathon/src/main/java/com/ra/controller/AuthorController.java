package com.ra.controller;

import com.ra.model.Author;
import com.ra.model.PaginationResult;
import com.ra.service.AuthorService;
import com.ra.service.imp.CloudinaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {
    private static final int AUTHOR_PER_PAGE = 10;

    private final AuthorService authorService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public AuthorController(AuthorService authorService, CloudinaryService cloudinaryService) {
        this.authorService = authorService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public String showAllAuthor(@RequestParam(name = "page", defaultValue = "1") int page,
                                Model model) {
        PaginationResult<Author> authorList = authorService.getPaginationData(AUTHOR_PER_PAGE, page);
        model.addAttribute("authors", authorList.getDataList());
        model.addAttribute("totalPages", authorList.getTotalPages());
        model.addAttribute("currentPage", page);
        return "author/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("author", new Author());
        return "author/add";
    }

    @PostMapping("/add")
    public String addAuthor(@Valid @ModelAttribute("author") Author author,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            boolean unique = authorService.isAuthorNameUnique(author.getId(), author.getName());
            if (!unique) {
                result.rejectValue("name", "error.author", "Tên tác giả đã tồn tại");
            }
        }
        if (result.hasErrors()) {
            return "author/add";
        }
        boolean addSuccess = authorService.saveAuthor(author);
        if (addSuccess) {
            redirectAttributes.addFlashAttribute("successMsg", "Thêm tác giả thành công");
            return "redirect:/authors";
        } else {
            model.addAttribute("errorMsg", "Thêm tác giả thất bại");
            return "author/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Author author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        return "author/edit";
    }

    @PostMapping("/edit")
    public String editAuthor(@Valid @ModelAttribute("author") Author author,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            boolean unique = authorService.isAuthorNameUnique(author.getId(), author.getName());
            if (!unique) {
                result.rejectValue("name", "error.author", "Tên tác giả đã tồn tại");
            }
        }
        if (result.hasErrors()) {
            return "author/edit";
        }
        boolean editSuccess = authorService.updateAuthor(author);
        if (editSuccess) {
            redirectAttributes.addFlashAttribute("successMsg", "Cập nhật tác giả thành công");
            return "redirect:/authors";
        } else {
            model.addAttribute("errorMsg", "Cập nhật tác giả thất bại");
            return "author/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable("id") int id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        // Kieemr tra tac gia co sach nao khong
        boolean deleteSuccess = authorService.deleteAuthor(id);
        if (deleteSuccess) {
            redirectAttributes.addFlashAttribute("successMsg", "Xóa tác giả thành công");
            return "redirect:/authors";
        } else {
            model.addAttribute("errorMsg", "Xóa tác giả thất bại");
            return "author/list";
        }
    }

    @GetMapping("/search")
    public String searchAuthor(@RequestParam("keyword") String keyword, Model model) {
        List<Author> authorList = authorService.findAuthorByField(keyword);
        model.addAttribute("authors", authorList);
        model.addAttribute("keyword", keyword);

        String imageUrl = cloudinaryService.uploadFile()

        return "author/list";
    }
}



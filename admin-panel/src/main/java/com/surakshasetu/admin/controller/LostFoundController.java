
package com.surakshasetu.admin.controller;

import com.surakshasetu.admin.service.LostFoundApiService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class LostFoundController {

    private final LostFoundApiService lostFoundApiService;

    public LostFoundController(
            LostFoundApiService lostFoundApiService) {
        this.lostFoundApiService = lostFoundApiService;
    }

    // Dashboard
    @GetMapping("/lostfound")
    public String lostFoundDashboard(Model model) {

        var items = lostFoundApiService.getAllItems();

        model.addAttribute("items", items);
        model.addAttribute("totalReports", items.size());

        model.addAttribute("lostItems",
                items.stream()
                        .filter(item -> "LOST".equals(item.get("type")))
                        .count());

        model.addAttribute("foundItems",
                items.stream()
                        .filter(item -> "FOUND".equals(item.get("type")))
                        .count());

        model.addAttribute("returnedItems",
                items.stream()
                        .filter(item -> "RETURNED".equals(item.get("status")))
                        .count());

        return "lostfound/index";
    }

    // Update Item
    @PostMapping("/lostfound/update/{id}")
    public String updateItem(
            @PathVariable Long id,
            @RequestParam Map<String, Object> item,
            RedirectAttributes redirectAttributes) {

        lostFoundApiService.updateItem(id, item);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Item updated successfully!");

        return "redirect:/lostfound";
    }

    // Delete Item
    @PostMapping("/lostfound/delete/{id}")
    public String deleteItem(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        lostFoundApiService.deleteItem(id);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Item deleted successfully!");

        return "redirect:/lostfound";
    }
}

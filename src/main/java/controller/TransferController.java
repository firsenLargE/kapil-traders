package controller;

import model.Transfer;
import service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TransferController {
    @Autowired
    private TransferService transferService;

    @GetMapping("/transfers")
    public String listTransfers(Model model) {
        try {
            model.addAttribute("transfers", transferService.getAllTransfers());
        } catch (Exception e) {
            model.addAttribute("transfers", java.util.Collections.emptyList());
            model.addAttribute("error", "Error loading transfers: " + e.getMessage());
        }
        return "transfers";
    }

    @GetMapping("/transfers/new")
    public String newTransferForm(Model model) {
        model.addAttribute("transfer", new Transfer());
        return "transfers";
    }

    @PostMapping("/transfers")
    public String saveTransfer(@ModelAttribute Transfer transfer) {
        try {
            transfer.setStatus("Pending");
            transferService.saveTransfer(transfer);
        } catch (Exception e) {
            System.err.println("Error saving transfer: " + e.getMessage());
        }
        return "redirect:/transfers";
    }

    @GetMapping("/transfers/cancel/{id}")
    public String cancelTransfer(@PathVariable Long id) {
        try {
            transferService.cancelTransfer(id);
        } catch (Exception e) {
            System.err.println("Error canceling transfer: " + e.getMessage());
        }
        return "redirect:/transfers";
    }
}

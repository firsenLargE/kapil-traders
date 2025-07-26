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
        model.addAttribute("transfers", transferService.getAllTransfers());
        return "transfers";
    }

    @GetMapping("/transfers/new")
    public String newTransferForm(Model model) {
        model.addAttribute("transfer", new Transfer());
        return "transfers";
    }

    @PostMapping("/transfers")
    public String saveTransfer(@ModelAttribute Transfer transfer) {
        transfer.setStatus("Pending");
        transferService.saveTransfer(transfer);
        return "redirect:/transfers";
    }

    @GetMapping("/transfers/cancel/{id}")
    public String cancelTransfer(@PathVariable Long id) {
        transferService.cancelTransfer(id);
        return "redirect:/transfers";
    }
}

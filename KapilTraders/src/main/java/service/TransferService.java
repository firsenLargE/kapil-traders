package service;

import model.Transfer;
import java.util.List;

public interface TransferService {
    List<Transfer> getAllTransfers();
    Transfer saveTransfer(Transfer transfer);
    void cancelTransfer(Long id);
}
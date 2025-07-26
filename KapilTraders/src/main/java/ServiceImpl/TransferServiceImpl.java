package ServiceImpl;

import model.Transfer;
import repository.TransferRepository;
import service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    private TransferRepository transferRepository;

    @Override
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    @Override
    public Transfer saveTransfer(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    @Override
    public void cancelTransfer(Long id) {
        Transfer transfer = transferRepository.findById(id).orElseThrow();
        transfer.setStatus("Cancelled");
        transferRepository.save(transfer);
    }
}

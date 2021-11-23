package br.com.sw2you.realmeet.controller;

import br.com.sw2you.realmeet.api.facade.AllocationsApi;
import br.com.sw2you.realmeet.api.model.*;
import br.com.sw2you.realmeet.service.AllocationService;
import br.com.sw2you.realmeet.util.ResponseEntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

@RestController
public class AllocationController implements AllocationsApi {
    private final Executor controllerExecutor;
    private final AllocationService allocationService;

    public AllocationController(Executor controllerExecutor, AllocationService allocationService) {
        this.allocationService = allocationService;
        this.controllerExecutor = controllerExecutor;
    }

    @Override
    public CompletableFuture<ResponseEntity<Void>> deleteAllocation(Long id) {
        return runAsync(() -> allocationService.deleteAllocation(id), controllerExecutor).thenApply(ResponseEntityUtils::noContent);
    }

    @Override
    public CompletableFuture<ResponseEntity<AllocationDTO>> createAllocation(CreateAllocationDTO createAllocationDTO) {
        return supplyAsync(() -> allocationService.createAllocation(createAllocationDTO), controllerExecutor)
            .thenApply(ResponseEntityUtils::created);
    }
}

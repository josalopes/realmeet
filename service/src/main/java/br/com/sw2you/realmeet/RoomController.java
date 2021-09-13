package br.com.sw2you.realmeet;


import br.com.sw2you.realmeet.api.facade.RoomsApi;
import br.com.sw2you.realmeet.api.model.Room;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class RoomController implements RoomsApi {
    @Override
    public CompletableFuture<ResponseEntity<Room>> listRooms(Long id) {
        return supplyAsync(() -> ResponseEntity.ok(new Room().id(1L).name("Room #1")));
    }
}

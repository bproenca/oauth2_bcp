/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcp.albums.controllers;

import bcp.albums.response.AlbumRest;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albums")
public class AlbumsController {
    
    @GetMapping
    public List<AlbumRest> getAlbums() { 
        
        AlbumRest album1 = new AlbumRest();
        album1.setAlbumId("albumIdHere");
        album1.setUserId("1");
        album1.setAlbumTitle("Album one title REST " + System.currentTimeMillis());
        album1.setAlbumDescription("Album 1 description REST");
        album1.setAlbumUrl("Album 1 URL REST");
        
        AlbumRest album2 = new AlbumRest();
        album2.setAlbumId("albumIdHere");
        album2.setUserId("2");
        album2.setAlbumTitle("Album two title REST " + System.currentTimeMillis());
        album2.setAlbumDescription("Album 2 description REST");
        album2.setAlbumUrl("Album 2 URL REST");
         
        return Arrays.asList(album1, album2);
    }
 
}

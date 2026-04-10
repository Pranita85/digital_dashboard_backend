//package com.tatamotors.classbooking.controller;
//
//import com.tatamotors.classbooking.entity.SystemSettings;
//import com.tatamotors.classbooking.repository.SystemSettingsRepository;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/settings")
//@CrossOrigin
//public class SettingsController {
//
//    private final SystemSettingsRepository settingsRepository;
//
//    public SettingsController(SystemSettingsRepository settingsRepository) {
//        this.settingsRepository = settingsRepository;
//    }
//
//    // GET current settings
//    @GetMapping
//    public ResponseEntity<SystemSettings> getSettings() {
//        // Always return row with id=1, create default if not exists
//        SystemSettings settings = settingsRepository.findById(1)
//                .orElseGet(() -> {
//                    SystemSettings defaults = new SystemSettings();
//                    defaults.setId(1);
//                    return settingsRepository.save(defaults);
//                });
//        return ResponseEntity.ok(settings);
//    }
//
//    // PUT save settings
//    @PutMapping
//    public ResponseEntity<SystemSettings> saveSettings(@RequestBody SystemSettings updated) {
//        updated.setId(1); // always update the single row
//        return ResponseEntity.ok(settingsRepository.save(updated));
//    }
//}

package com.tatamotors.classbooking.controller;

import com.tatamotors.classbooking.entity.SystemSettings;
import com.tatamotors.classbooking.repository.SystemSettingsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin
public class SettingsController {

    private final SystemSettingsRepository settingsRepository;

    public SettingsController(SystemSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    // GET current settings
    @GetMapping
    public ResponseEntity<SystemSettings> getSettings() {
        // Always return row with id=1, create default if not exists
        SystemSettings settings = settingsRepository.findById(1)
                .orElseGet(() -> {
                    SystemSettings defaults = new SystemSettings();
                    defaults.setId(1);
                    return settingsRepository.save(defaults);
                });
        return ResponseEntity.ok(settings);
    }

    // PUT save settings
    @PutMapping
    public ResponseEntity<SystemSettings> saveSettings(@RequestBody SystemSettings updated) {
        updated.setId(1); // always update the single row
        return ResponseEntity.ok(settingsRepository.save(updated));
    }
}
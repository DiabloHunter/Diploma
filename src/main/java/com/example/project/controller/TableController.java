package com.example.project.controller;

import com.example.project.common.ApiResponse;
import com.example.project.dto.table.CreateTableDto;
import com.example.project.dto.table.UpdateTableDto;
import com.example.project.service.ITableService;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/table")
public class TableController {

    @Autowired
    ITableService tableService;

    //todo add getTablesByTime
    private static final Logger LOG = LogManager.getLogger(TableController.class);

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createTable(@RequestBody CreateTableDto createTableDto) {
        try {
            tableService.create(createTableDto);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        LOG.info(String.format("Table with searchId %s has been created!", createTableDto.getSearchId()));
        return new ResponseEntity<>(new ApiResponse(true, String.format("Table with searchId %s has been created!",
                createTableDto.getSearchId())), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateTable(@RequestBody UpdateTableDto updateTableDto) {
        try {
            tableService.update(updateTableDto);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        LOG.info(String.format("Table with searchId %s has been updated!", updateTableDto.getPreviousSearchId()));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Table with searchId %s has been updated!", updateTableDto.getPreviousSearchId())), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestParam String searchId) {
        if (!tableService.existBySearchId(searchId)) {
            return new ResponseEntity<>(new ApiResponse(false, "Table does not exists"), HttpStatus.NOT_FOUND);
        }
        try {
            tableService.deleteTable(searchId);
        } catch (NotFoundException e) {
            LOG.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }

        LOG.info(String.format("Table with searchId %s has been deleted!", searchId));
        return new ResponseEntity<>(new ApiResponse(true,
                String.format("Table with searchId %s has been deleted!", searchId)), HttpStatus.OK);
    }


//    @GetMapping("/")
//    public ResponseEntity<List<TableTimeDto>> getAllTables() {
//        List<TableTimeDto> tableTimeDtos = tableService.getAllTablesDto();
//        return new ResponseEntity<>(tableTimeDtos, HttpStatus.OK);
//    }
//
//    @GetMapping("/searchId")
//    public ResponseEntity<TableTimeDto> getTableBySearchId(@RequestParam String searchId) {
//        TableTimeDto tableTimeDto = tableService.getTableDtoBySearchId(searchId);
//        return new ResponseEntity<>(tableTimeDto, HttpStatus.OK);
//    }
//
//    @GetMapping("/seats")
//    public ResponseEntity<List<TableTimeDto>> getTablesBySeats(@RequestParam Integer seats) {
//        List<TableTimeDto> tables = tableService.getTablesByNumberOfSeats(seats);
//        return new ResponseEntity<>(tables, HttpStatus.OK);
//    }
//    @GetMapping("/test")
//    public ResponseEntity<ApiResponse> test() {
//        tableService.create(new CreateTableDto("searchId", 4));
//        return new ResponseEntity<>(new ApiResponse(true, String.format("Table with searchId %s has been created!",
//                "searchId")), HttpStatus.CREATED);
//    }
//
//    @GetMapping("/test1")
//    public ResponseEntity<Reservation> test1() {
//        List<Table> tables = tableService.getAllTables();
//        Reservation reservation = new Reservation();
//        reservation.setTables(tables);
//        return new ResponseEntity<>(reservation, HttpStatus.OK);
//    }
//
//    @Scheduled(cron = "${cron.check.table.reservation.time1}")
//    private void test2() {
//        getAllTables();
//    }

//    @GetMapping(
//            value = "/get-image-with-media-type"
//    )
//    public Graphics2D getImageWithMediaType1() {
//        File file = new File("/com/example/project/util/pngwing.png");
//        InputStream in = getClass()
//                .getResourceAsStream("/com/example/project/util/pngwing.png");
//        return IOUtils.toByteArray(String.valueOf(file));
//        BufferedImage image = null;
//        try {
//            String path = "\\images\\pngwing.png";
//            InputStream is = TableController.class.getClassLoader().getResourceAsStream(path);
//            image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
//            image = ImageIO.read(is);
//            return image.createGraphics();
//        }
//        catch (IOException e) {
//            System.out.println("Error: " + e);
//        }
//        return null;
//    }

//    @GetMapping(value = "/get-image-with-media-type")
//    public @ResponseBody
//    Map<String, String> getImage() throws IOException {
//        String path = "\\images\\default.png";
//        InputStream is = TableController.class.getClassLoader().getResourceAsStream(path);
////        File file = new ClassPathResource(imagesPath + imageName).getFile();
//        String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(is.readAllBytes());
//        Map<String, String> jsonMap = new HashMap<>();
//        jsonMap.put("content", encodeImage);
//
//        return jsonMap;
//    }

//    @GetMapping(value = "/saveImage")
//    public void getImageWithMediaType() throws IOException {
//        InputStream fileContent = filePart.getInputStream();
//        BufferedImage image = ImageIO.read(fileContent);
//        ImageIO.write (image, fileName.split("\\.")[1], new File(pictureSave));
//}


//    @GetMapping("/get-image-dynamic-type")
//    @ResponseBody
//    public ResponseEntity<InputStreamResource> getImageDynamicType(@RequestParam("jpg") boolean jpg) {
//        MediaType contentType = jpg ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG;
//        InputStream in = jpg ?
//                getClass().getResourceAsStream("/com/baeldung/produceimage/image.jpg") :
//                getClass().getResourceAsStream("/com/baeldung/produceimage/image.png");
//        return ResponseEntity.ok()
//                .contentType(contentType)
//                .body(new InputStreamResource(in));
//    }

}

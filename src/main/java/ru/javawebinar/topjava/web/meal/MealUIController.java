package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@RestController
@RequestMapping(value = "/admin/meals", produces = MediaType.APPLICATION_JSON_VALUE)
public class MealUIController extends AbstractMealController {

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestParam String dateTime,
                       @RequestParam String description,
                       @RequestParam int calories) {
        super.create(new Meal(LocalDateTime.parse(dateTime), description, calories));
    }

    @GetMapping("/filter")
    public List<MealTo> getBetween(@RequestParam @Nullable String startDate,
                                   @RequestParam @Nullable String startTime,
                                   @RequestParam @Nullable String endDate,
                                   @RequestParam @Nullable String endTime) {
        LocalDate parsedStartDate = parseLocalDate(startDate);
        LocalDate parsedEndDate = parseLocalDate(endDate);
        LocalTime parsedStartTime = parseLocalTime(startTime);
        LocalTime parsedEndTime = parseLocalTime(endTime);
        return super.getBetween(parsedStartDate, parsedStartTime, parsedEndDate, parsedEndTime);
    }
}

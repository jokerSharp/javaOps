package ru.javawebinar.topjava.web.meal;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping()
    public String getAllFiltered(Model model,
                                 @Nullable @RequestParam(value = "startDate", required = false) String startDate,
                                 @Nullable @RequestParam(value = "startTime", required = false) String startTime,
                                 @Nullable @RequestParam(value = "endDate", required = false) String endDate,
                                 @Nullable @RequestParam(value = "endTime", required = false) String endTime) {
        return super.getAllFiltered(model, startDate, startTime, endDate, endTime);
    }

    @PostMapping()
    public String addMeal(HttpServletRequest request) {
        return super.addMeal(request);
    }

    @DeleteMapping()
    public String deleteMeal(@RequestParam("id") Integer id) {
        return super.deleteMeal(id);
    }

    @GetMapping("/form")
    public String addMeal(@RequestParam(value = "id", required = false) Integer id, Model model) {
        return super.addMeal(id, model);
    }
}

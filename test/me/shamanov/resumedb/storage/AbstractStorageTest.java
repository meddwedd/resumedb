package me.shamanov.resumedb.storage;

import me.shamanov.resumedb.model.*;
import me.shamanov.resumedb.model.Establishment.Period;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static me.shamanov.resumedb.model.ContactType.*;
import static me.shamanov.resumedb.model.SectionType.*;

/**
 * Author: Mike
 * Date: 31.03.2019
 */

public abstract class AbstractStorageTest {
    Storage storage;
    private List<Resume> resumes;
    private Resume resume0, resume1, resume2, resume3;

    @Before
    public void setUp() {
        resumes = new ArrayList<Resume>() {
            {
                add(Resume.of("Александр Ковров", "Москва", 30));
                add(Resume.of("Михаил Иванов", "Санкт-Петербург", 21));
                add(Resume.of("Анна Егорова", "Кострома", 33));
//                add(Resume.of("Ксения Ермолова", "Ярославль", 25));
            }
        };
        //Filling resumes with information
        //Resume at index 0
        resume0 = resumes.get(0);
        resume0.addContact(EMAIL, "kovrov@yandex.ru");
        resume0.addContact(MOBILE, "+7 910 999 22 33");
        resume0.addContact(VK, "http://vk.com");
        resume0.addSection(POSITION, MultipleTextHolder.from("Java разработчик", "начинающий"));
        resume0.addSection(ACCOMPLISHMENT,
                MultipleTextHolder.from(
                        "Разработал интернет-магазин для Dummy Co. Ltd. с нуля.",
                        "Разработал web-framework на основе EJB3, который вошёл в топ 5 перспективных фреймворков на Java, но это не точно.",
                        "Участвовал в создании искусственного интеллекта для робота \"Алёша\""
                ));
        resume0.addSection(EDUCATION, EstablishmentHolder.from(
                Establishment.of("ЯрГУ", Period.of("01/2012", "01/2017", "Програмимист"), Period.of("01/2017", "01/2018", "Аспирант")),
                Establishment.of("Udemy Java Course", Period.of("01/2017", "01/2018", "Java Junior Programmer"))
        ));
        resume0.addSection(EXPERIENCE, EstablishmentHolder.from(
                Establishment.of("Doomble", Period.of("03/2013", "05/2015", "Програмимер", "Уже давали что-то писать", "Тестировал игры")),
                Establishment.of("ООО \"СтартАп\"", Period.of("06/2015", "03/2018", "Недопрограммер", "Ставил программки", "Устанавливал Windows"))
        ));
        //Resume at index 1
        resume1 = resumes.get(1);
        resume1.addContact(EMAIL, "ivanov@gmail.com");
        resume1.addContact(MOBILE, "+7 960 777 11 22");
        resume1.addSection(POSITION, MultipleTextHolder.from("Таможенный представитель"));
        resume1.addSection(ACCOMPLISHMENT,
                MultipleTextHolder.from(
                        "Работал с различным ПО для формирования всего пакета необхоидмых документов для оформления ДТ и прочих непонятных аббревиатур.",
                        "Осуществлял удалённое дкларирование по всей России, даже на Аляске.",
                        "Написал нескольго небольших программных оболочек на основе Excel для облегчения процесса работы, благодаря этому начальство сократило 10 сотрудников."
                ));

        //Resume at index 2
        resume2 = resumes.get(2);
        resume2.addContact(EMAIL, "egorova@mail.ru");
        resume2.addContact(MOBILE, "+7 915 987 99 25");
        resume2.addSection(POSITION, MultipleTextHolder.from("Химик-лаборант"));
        resume2.addSection(ACCOMPLISHMENT,
                MultipleTextHolder.from(
                        "Разработала новый вкус пива с использованием натуральных зерён кофе под маркой Java",
                        "Участвовала в процессе обучения новых сотрудников на позицию Химик-отравитель",
                        "Участвовала в согласовании работы двух предприятий - по производству колбасных изделий и туалетной бумаги."
                ));

/*        //Resume at index 3
        resume3 = resumes.get(3);
        resume3.addContact(ContactType.EMAIL, "");
        resume3.addContact(ContactType.MOBILE, "");
        resume3.addSection(SectionType.POSITION, MultipleTextHolder.from(""));
        resume3.addSection(SectionType.ACCOMPLISHMENT,
                MultipleTextHolder.from(
                        "",
                        "",
                        ""
                ));*/

        storage.clear();

        for (Resume r : resumes) {
            storage.save(r);
        }
    }

    @Test
    public void testUpdate() {
        resume0.setFullName("Александр Ершов");
        storage.update(resume0);
        Assert.assertEquals(resume0, storage.load(resume0.getId()));
    }

    @Test
    public void testSave() {
        Resume resume3 = Resume.of("Дмитрий Позов", "Пермь", 37);
        storage.save(resume3);
        Assert.assertEquals(resume3, storage.load(resume3.getId()));
    }

    @Test
    public void testLoad() {
        Resume load = storage.load(resume0.getId());
        Assert.assertEquals(resume0, load);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelete() {
        storage.delete(resume2.getId());
        storage.load(resume2.getId());
    }

    @Test
    public void testSize() {
        int size = storage.size();
        Assert.assertEquals(3, size);
        Resume resume3 = Resume.of("Дмитрий Позов", "Пермь", 24);
        storage.save(resume3);
        size = storage.size();
        Assert.assertEquals(4, size);
    }

    @Test
    public void testClear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void getSortedResumeList() {
        Collections.sort(resumes);
        List<Resume> sortedFromStorage = storage.getSortedResumeList();
        Assert.assertEquals(resumes, sortedFromStorage);
    }
}
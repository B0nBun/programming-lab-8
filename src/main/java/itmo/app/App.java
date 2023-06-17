package itmo.app;

import com.google.common.base.Function;
import javax.swing.JButton;
import javax.swing.JFrame;

/*
Графический интерфейс клиентской части должен поддерживать русский, исландский, французкий и английский (Южная Африка) языки / локали. Должно обеспечиваться корректное отображение чисел, даты и времени в соответстии с локалью. Переключение языков должно происходить без перезапуска приложения. Локализованные ресурсы должны храниться в классе.



Заменить консольный клиент на клиент с графическим интерфейсом пользователя(GUI). 
В функционал клиента должно входить:

    - Окно с авторизацией/регистрацией.
    - Отображение текущего пользователя.
    - Таблица, отображающая все объекты из коллекции
        - Каждое поле объекта - отдельная колонка таблицы.
        - Строки таблицы можно фильтровать/сортировать по значениям любой из колонок. Сортировку и фильтрацию значений столбцов реализовать с помощью Streams API.
    - Поддержка всех команд из предыдущих лабораторных работ.
        - help
        - show
        - add
        - clear
        - head
        - info
        - remove_by_id
        - count_greater_than_fuel_type
        - group_counting_by_id
        - add_if_max
        - filter_greater_than
        - remove_lower
        - update
    - Область, визуализирующую объекты коллекции
        - Объекты должны быть нарисованы с помощью графических примитивов с использованием Graphics, Canvas или аналогичных средств графической библиотеки.
        - При визуализации использовать данные о координатах и размерах объекта.
        - Объекты от разных пользователей должны быть нарисованы разными цветами.
        - При нажатии на объект должна выводиться информация об этом объекте.
        - При добавлении/удалении/изменении объекта, он должен автоматически появиться/исчезнуть/измениться  на области как владельца, так и всех других клиентов. 
        - При отрисовке объекта должна воспроизводиться согласованная с преподавателем анимация.
    - Возможность редактирования отдельных полей любого из объектов (принадлежащего пользователю). Переход к редактированию объекта возможен из таблицы с общим списком объектов и из области с визуализацией объекта.
    - Возможность удаления выбранного объекта (даже если команды remove ранее не было).

Перед непосредственной разработкой приложения необходимо согласовать прототип интерфейса с преподавателем. Прототип интерфейса должен быть создан с помощью средства для построения прототипов интерфейсов(mockplus, draw.io, etc.) 
*/

public class App {

    public static void call(Function<Integer, ?> f) {
        f.apply(2);
    }

    public static void main(String[] args) {
        var frame = new JFrame();
        frame.add(new JButton("sad"));
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

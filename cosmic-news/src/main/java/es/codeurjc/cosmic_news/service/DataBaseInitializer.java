package es.codeurjc.cosmic_news.service;

import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.codeurjc.cosmic_news.model.Badge;
import es.codeurjc.cosmic_news.model.Event;
import es.codeurjc.cosmic_news.model.News;
import es.codeurjc.cosmic_news.model.Picture;
import es.codeurjc.cosmic_news.model.Question;
import es.codeurjc.cosmic_news.model.Quizz;
import es.codeurjc.cosmic_news.model.User;
import es.codeurjc.cosmic_news.model.Video;
import es.codeurjc.cosmic_news.repository.EventRepository;
import es.codeurjc.cosmic_news.repository.NewsRepository;
import es.codeurjc.cosmic_news.repository.PictureRepository;
import es.codeurjc.cosmic_news.repository.QuizzRepository;
import es.codeurjc.cosmic_news.repository.UserRepository;
import es.codeurjc.cosmic_news.repository.VideoRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DataBaseInitializer {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired 
    private EventRepository eventRepository;

    @Autowired 
    private VideoRepository videoRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initDatabase(){
        initQuizzes();
        initUsers();
        initPictures();
        initNews();
        initEvents();
        initVideos();
    }

    private void initUsers(){
        User user1 = new User(
            "Juan",
            "Saturno",
            "Usuario inicializado",
            "JuanitoSaturno42",
            "juanSaturno@gmail.com",
            passwordEncoder.encode("juan"),
            "USER");
        
        User user2 = new User(
            "Pepe",
            "Jupiter",
            "Usuario inicializado",
            "PepitoInJupiter",
            "a",
            passwordEncoder.encode("a"),
            "USER");
        
        User user3 = new User(
            "Pedro",
            "Admin",
            "Admin inicializado",
            "GodPedro",
            "xd",
            passwordEncoder.encode("xd"),
            "USER","ADMIN");

        Blob photo = null;
        photo = photoToBlob("static/images/logo2.png");
        user3.addBadge(new Badge(photo,"Bienvenida", 0));
 
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        
    }

    private void initNews(){
        News news1 = new News(
            "Muere el legendario artista espacial Adolf Schaller a los 68 años",
            "La comunidad astronómica ha perdido a uno de los grandes artistas visuales de la historia, que se dedicaba a las ilustraciones astronómicas.",
            "Don Davis",
            "Ciencia",
            "Uno de los artistas espaciales pioneros y más talentosos del mundo, Adolf Schaller, murió a principios de agosto a los 68 años. Deja una obra publicada que lo establece como uno de los más grandes artistas espaciales. Su atención a los descubrimientos en curso, desde el sistema solar hasta galaxias distantes, dio como resultado representaciones que reflejan tanto lo que se sabe sobre estos lugares como su visión de su atractivo visual.\n\n"
        + "Nacido en 1956, la vida de Schaller abarcó la era espacial desde sus inicios con el Sputnik 1 hasta muchos descubrimientos recientes en el espacio cercano y distante.\n\n"
        + "A mediados de la década de 1970, la revista Astronomy fue una presentadora pionera del arte espacial, y los trabajos anteriores de Schaller vistos allí formaron una gran parte de la “edad de oro” inicial de esa revista. Un editor de Astronomy, Terence Dickinson, publicó posteriormente varias ediciones de un magnífico libro, The Universe and Beyond (Camden House, 1992), que contenía numerosas ilustraciones de Schaller, muchas de ellas ejecutadas específicamente para el libro.\n\n"
        + "Más tarde, Schaller pasó a formar parte del equipo de artistas del programa Cosmos de PBS de Carl Sagan y creó muchas obras de arte de animación, así como globos pintados y fondos de modelos de paisajes para la serie. Más tarde prestó su talento a los programas de PBS Planet Earth e Infinite Voyage. Schaller también produjo muchas capas de arte para un breve acercamiento al centro galáctico para la película Brainstorm de Douglas Trumbull. La aportación de Schaller al paisaje de Europa para la película 2010 fue demasiado exigente para el director, que lo despidió cuando los conocimientos informados del artista chocaron con las ideas preconcebidas del director.\n\n"
        + "El arte espacial de Schaller apareció más tarde en comunicados de prensa de muchos observatorios y en exhibiciones rediseñadas para el Observatorio Griffith en Los Ángeles. Un libro de Dickinson y Schaller, Extraterrestrials: A Field Guide for Earthlings (Camden House, 1994), muestra además la amplitud de sus habilidades conceptuales y artísticas.\n\n"
        + "Echaremos de menos la visión de Adolf Schaller, pero su trabajo seguirá inspirando.",
            5,
            LocalDate.of(2024,8,9)                  
        );

        Blob photo = null;
        photo = photoToBlob("static/images/adolfSchaller.jpeg");
        news1.setPhoto(photo);
        news1.setImage(photo != null);

        News news2 = new News(
            "El primer mapa de Io de volcanes insinúa un océano de magma subterráneo",
            "El nuevo mapa también destaca los extraordinarios efectos del calentamiento de las mareas que sufre la luna, gracias a su configuración orbital.",
            "Theo Nicitopoulos",
            "Sistema Solar",
            "Io, uno de los satélites galileanos de Júpiter, es el cuerpo volcánicamente más activo del sistema solar. Imágenes de naves espaciales han mostrado lava fundida en erupción a lo largo de paredes que represan lagos de lava gigantes e imponentes columnas de gas y polvo que se elevan desde amplias calderas.\n\n"
        + "Ahora, un equipo dirigido por la científica planetaria Ashley Davies del Laboratorio de Propulsión a Chorro de la NASA ha preparado el primer mapa global de flujo de calor de la actividad volcánica de Ío. El nuevo mapa muestra los lugares donde ha surgido magma desde el interior de la luna. Los resultados, publicados en The Planetary Science Journal, apuntan a un posible océano de magma en el interior del satélite y resaltan cómo las resonancias comunes en las órbitas de los cuerpos celestes podrían conducir a otros océanos en el sistema solar y más allá.\n"
        + "El equipo encontró 87 puntos calientes previamente desconocidos utilizando datos de la nave espacial Juno de la NASA y 14 sitios adicionales de este tipo a partir de datos recopilados con telescopios terrestres. Combinaron los nuevos sitios con los previamente conocidos para mapear un total de 343 fuentes de actividad volcánica en la luna.\n\n"
        + "Los resultados muestran, por primera vez, que los volcanes de Ío están distribuidos uniformemente a escala global, pero que el calor que emiten no. Los volcanes de las regiones polares emiten menos calor que los de latitudes más bajas. Además, los volcanes del polo norte de Ío liberan el doble de energía que los de la región del polo sur. \"Esa fue la mayor sorpresa\", dice Davies, \"un verdadero enigma que aún no hemos resuelto\".\n\n"
        + "La distribución desigual del calor, más en latitudes medias que en los polos, es consistente con los modelos que sugieren que el calentamiento en Io ocurre más cerca de su superficie y derrite la roca del subsuelo. Sin embargo, Davies dice que no está claro cuán extenso sería el derretimiento: \"No sabemos si se trata de un océano de magma global o parcial\".\n\n"
        + "Una de las razones por las que Ío es tan volcánicamente activo es que la luna está atrapada en un patrón repetitivo de configuraciones orbitales, llamadas resonancias orbitales, con dos de los otros satélites de Júpiter, Europa y Ganímedes. En particular, Europa tarda el doble en orbitar a Júpiter que Io, y Ganímedes tarda cuatro veces más que Io. Debido a estas resonancias, las lunas experimentan tirones gravitacionales que se repiten regularmente entre sí durante las alineaciones, lo que ha hecho que sus órbitas sean menos circulares. Por lo tanto, debido a que Ío se acerca y se aleja de Júpiter durante cada revolución, las fuerzas de marea estiran y comprimen la luna, produciendo calor.\n\n"
        + "\"Las fuerzas de marea crean fricción\", dice Giacomo Lari, matemático de la Universidad de Pisa en Italia y primer autor de otro artículo reciente sobre las lunas de Júpiter, publicado en Celestial Mechanics and Dynamical Astronomy. Y como Júpiter es tan masivo e Io orbita tan cerca del planeta, el calentamiento resultante de las mareas es extremo. El efecto ha disminuido pero sigue presente en Europa, cuya órbita se ha vuelto menos circular y también sufre calentamiento por mareas, lo que potencialmente permitiría que existiera un océano de agua debajo de su corteza helada.\n\n"
        + "Júpiter no es el único lugar donde tales efectos importan: también se cree que Encelado, la luna de Saturno, tiene un océano de agua, que puede mantenerse por el calentamiento de las mareas causado por las resonancias con la luna Dione.\n\n"
        + "\"Comprender cómo funcionan las resonancias y cómo la actividad resultante ha afectado a estas increíbles lunas es de vital importancia para comprender la evolución del sistema solar\", afirma Davies. Esto también incluye la historia del vulcanismo en la Tierra, la Luna y Marte, que probablemente experimentaron una actividad similar a la escala de Io desde el principio, añade Davies.\n\n"
        + "Tres no es una multitud\n"
        + "El artículo de Lari demostró que las órbitas de Io, Europa y Ganímedes están dominadas por la interacción combinada de los tres cuerpos, en lugar de resonancias separadas entre pares de lunas, un hallazgo que no era necesariamente obvio, dice Lari.\n\n"
        + "Añade que las tres lunas se encuentran actualmente en una \"zona segura\" donde las resonancias son estables y deberían seguir siéndolo durante al menos mil millones de años. Teniendo en cuenta que esta resonancia de tres cuerpos es la única conocida entre lunas del sistema solar, la oportunidad de observar los efectos de las mareas es extraordinaria.\n\n"
        + "Y los efectos también van mucho más allá de nuestro sistema solar. Los astrónomos han descubierto sistemas exoplanetarios donde varios planetas importantes orbitan en cadenas resonantes. En el futuro, tal vez los investigadores puedan examinar tales sistemas (o un sistema de exolunas alrededor de un solo exoplaneta) en busca de puntos calientes similares que podrían indicar calentamiento de las mareas y la formación de magma u océanos de agua.\n\n"
        + "Y mientras tanto, los nuevos resultados en Júpiter ciertamente harán que los investigadores anticipen más.",
    15,
            LocalDate.of(2024,8,6)   
        );

        photo = photoToBlob("static/images/vulcans.jpeg");
        news2.setPhoto(photo);
        news2.setImage(photo != null);

        News news3 = new News(
            "¿Qué es un eco de luz?",
            "Este fenómeno hace que los objetos se expandan más rápido que la velocidad de la luz, pero es un truco de la geometría cósmica.",                                  "Howard E.Bond",
            "Ciencia",
            "Felicitaciones por notar un efecto llamado expansión superluminal, un fenómeno que vemos en un eco de luz. Nos parece que el eco se expande muchas veces la velocidad de la luz, aparentemente violando el límite de velocidad fundamental en el universo.\n\n"
        + "Un eco de luz ocurre cuando una estrella experimenta una erupción o un objeto explota en una región rodeada de polvo interestelar. La luz de la erupción viaja directamente a la Tierra, por lo que eso es lo primero que vemos. Pero la luz también viaja hacia un lado, se refleja en el polvo y luego se dirige hacia nosotros. Debido a la distancia adicional recorrida, la luz reflejada llega a la Tierra más tarde, como la voz de un cantor que resuena en las montañas que los rodean.\n\n"
        + "Consideremos la imagen del Hubble del eco luminoso de la estrella en erupción V838 Monocerotis, tomada en octubre de 2002, unos 230 días después de que la luz de la explosión alcanzara su punto máximo en marzo de ese año. Sin embargo, el eco de luz en ese momento parece tener un radio de unos 3 años luz. ¿Cómo pudo la luz de la explosión viajar 3 años luz en menos de un año?\n\n"
        + "La respuesta está en la geometría de un eco de luz, ilustrada en el diagrama anterior. Aquí vemos V838 Mon en el centro, rodeado por una capa de polvo interestelar que se extiende en un radio de unos 6 o 7 años luz. En cualquier momento, el eco de la luz se encuentra sobre una superficie parabólica. Esta superficie marca el lugar en el espacio donde el tiempo adicional de viaje para que la luz rebote en el polvo y llegue a la Tierra es de 230 días. El polvo iluminado que parece estar a 3 años luz al lado de V838 Mon está en realidad a casi 6 años luz delante de la estrella. Entonces, es casi como si estuviéramos mirando un largo túnel en el polvo, con la estrella casi al final del túnel. ¡La velocidad de la luz no ha sido violada!\n\n"
        + "A medida que pasa el tiempo, el paraboloide se abre y se propaga hacia el fondo. Por lo tanto, cada observación del Hubble tomó muestras de una región diferente en el polvo circundante a medida que la iluminación la atravesaba. ¡Es un TAC astrofísico!",
            8,
            LocalDate.of(2024,8,4)   
        );

        photo = photoToBlob("static/images/light.jpeg");
        news3.setPhoto(photo);
        news3.setImage(photo != null);

        News news4 = new News(
            "¿Existe una temperatura más alta posible en el universo?",
            "El límite teórico puede ser lo que se llama temperatura de Planck, derivada utilizando constantes universales fundamentales.",
            "Sten Odenwald",
            "Cosmología",
            "Todos aceptamos que la temperatura más fría que puede alcanzar cualquier objeto es el cero absoluto. ¿Existe una temperatura máxima teórica que algo pueda alcanzar?\n\n"
        + "Esteban Kitterman\n"
        + "Thomasville, Carolina del Norte\n\n"
        + "La temperatura es una medida de la energía promedio de un conjunto de partículas. (Esto contrasta con la energía cinética, que es una medida de la energía transportada por una sola partícula). Entonces, para responder una pregunta sobre la temperatura, tenemos que encontrar algún conjunto de partículas para medir.\n\n"
        + "Una de esas colecciones se encuentra en el núcleo de nuestro Sol, donde se alcanzan temperaturas de unos 15 millones de kelvins (27 millones de grados Fahrenheit o 15 millones de grados Celsius). Las estrellas muy masivas pueden tener temperaturas centrales superiores a 500 millones de kelvins (900 millones de F o 500 millones de C). Justo antes de que una estrella masiva explote como supernova, algunos modelos teóricos predicen que su núcleo puede alcanzar temperaturas superiores a los 100 mil millones de Kelvin en el proceso de implosión, a medida que se acerca a la densidad extremadamente alta de una estrella de neutrones.\n\n"
        + "Antes de que el universo tuviera un segundo de edad, la materia era un plasma denso de nucleones y partículas elementales con densidades superiores a la del plomo. Podemos predecir la temperatura de este plasma, T, en kelvins usando la fórmula T = 1010/√t, donde t es el tiempo en segundos desde el Big Bang. En 10 microsegundos, la temperatura promedio del contenido del universo es de 3 billones de kelvins, equivalente a las energías que el Gran Colisionador de Hadrones explora en los plasmas de quarks y gluones (alrededor de 300 millones de electronvoltios). Pero dependiendo de qué tan atrás (es decir, más cerca en el tiempo del Big Bang) quieras retroceder, puedes obtener temperaturas aún más altas.\n\n",
            5,
            LocalDate.of(2024,7,18)   
        );

        photo = photoToBlob("static/images/temperature.jpg");
        news4.setPhoto(photo);
        news4.setImage(photo != null);

        News news5 = new News(
            "Cómo ver las Perseidas, la mejor lluvia de meteoritos de 2024",
            "Este año, las Perseidas ocurren sin Luna en el cielo de la mañana, lo que las eleva hasta convertirse probablemente en la mejor lluvia de meteoritos de 2024.",
            "Alison Klesman",
            "Próximos eventos",
            "Con altas tasas de meteoros y temperaturas agradables en verano, la lluvia de meteoritos de las Perseidas es una de las más celebradas del año. La lluvia de meteoritos de las Perseidas, que ocupa el segundo lugar en tasa máxima después de las Gemínidas de diciembre, este año ocupa un lugar central, gracias a la sincronización de las fases de la Luna.\n\n"
        + "Este año, la lluvia de meteoritos de las Perseidas alcanza su punto máximo el 12 de agosto con un primer cuarto de luna, que se pone antes de la medianoche para dejar las primeras horas de la mañana, las mejores para disfrutar del espectáculo, oscuras y sin luna. Por el contrario, el pico de las Gemínidas el 14 de diciembre de este año coincide con una Luna casi llena, que proyecta su luz brillante que oculta los meteoritos desde aproximadamente el anochecer hasta el amanecer.\n\n"
        + "Eso significa que las Perseidas se perfilan como la mejor lluvia de meteoritos del año, que no debe perderse. Aquí se explica cómo ver el programa.\n\n"
        + "Cuando ver las Perseidas\n"
        + "El pico oficial de las Perseidas ocurre el 12 de agosto en horas de la mañana para los observadores en los EE. UU. Esto coincide con el mejor momento en general para captar una lluvia de meteoritos, ya que por la mañana la Tierra gira hacia la corriente de escombros que causa la lluvia, lo que da como resultado trenes de meteoritos más largos que surcan el cielo. Sin embargo, la mañana anterior y la mañana posterior al pico (los días 11 y 13) también contarán con altas tasas de meteoros Perseidas, por lo que puedes extender tu observación durante varios días o planificar copias de seguridad si la fecha pico específica está nublada o es inconveniente.\n\n"
        + "Cómo ver las Perseidas\n"
        + "No necesitas ningún equipo para observar una lluvia de meteoritos; de hecho, tus ojos son mejores, ya que puedes escanear una gran región del cielo rápidamente sin sacrificar el campo de visión por la ampliación. Como mucho, es posible que desees un sillón o una manta, así como algo para mantenerte hidratado y quizás algún repelente de insectos, dependiendo de tu ubicación.\n\n"
        + "Los meteoros Perseidas parecen provenir de un punto específico, o radiante, en la constelación de Perseo, que alcanza aproximadamente 60° sobre el horizonte una hora antes del amanecer de esta semana. Cuanto más alto sea el radiante, más meteoros verás: las lluvias de meteoritos se caracterizan por su velocidad horaria cenital máxima, que estima la cantidad de meteoros visibles si el radiante estuviera directamente sobre tu cabeza (90°). Teniendo en cuenta la altura que alcanzará el radiante de las Perseidas antes del amanecer, la velocidad máxima de la lluvia de unos 100 meteoros por hora debería atenuarse sólo en un 15 por ciento aproximadamente, lo que significa que seguirá produciendo más de 80 meteoros por hora. ¡Eso es más de un meteoro por minuto!\n\n"
        + "(Tenga en cuenta, sin embargo, que eso es un promedio: es más probable que vea muchos meteoros en un corto período de tiempo, seguidos de una breve pausa, que exactamente la misma cantidad de meteoros cada minuto en una hora).\n\n"
        + "Para encontrar a Perseo, mira hacia el este una o dos horas antes del amanecer. Verás a Géminis y Orión recién ascendiendo, con la constelación en forma de V de Tauro sobre el patrón curvo de estrellas que delinea el arco de Orión. Actualmente, Tauro alberga tres planetas, incluidos los brillantes Marte y Júpiter, que están a solo unos días de una conjunción cercana el día 14. Se encuentran a la izquierda de la brillante estrella de color rojo anaranjado Aldebarán, que sirve como ojo de Tauro. (Consulte nuestra columna Sky This Week para obtener más detalles sobre cómo observar esta conjunción y esté atento a nuestro sitio para obtener información adicional a medida que se acerca el evento).\n\n"
        + "Por encima de Tauro en el cielo está Perseo, con el radiante de las Perseidas en la región norte de la constelación. El radiante está aproximadamente a 23° directamente encima de la brillante estrella Capella en la constelación de Auriga, en la parte inferior izquierda de Perseo.\n\n"
        + "Una vez que haya encontrado el área general del radiante, asegúrese de escanear entre 40° y 60° desde este punto. Debido a que las Perseidas parecen alejarse del radiante, verás los senderos más largos e impresionantes en el cielo a cierta distancia de este punto. Las Perseidas en particular son conocidas por sus trenes brillantes y duraderos. Como todos los meteoros, estas rayas son causadas por pequeñas motas de roca y polvo que se queman en nuestra atmósfera. Pero no te preocupes: los fragmentos de escombros que generan las lluvias de meteoritos son tan pequeños que se queman por completo antes de tocar el suelo.\n\n"
        + "Las Perseidas se generan a partir de restos de visitas anteriores del cometa 109P/Swift-Tuttle, que vuelve a girar alrededor del Sol cada 133 años. Su perihelio más reciente (paso cercano del Sol) fue en 1992, y no volverá a adornar nuestros cielos hasta 2125. Sin embargo, Swift-Tuttle es un cometa grande de unas 16 millas (26 kilómetros) de diámetro, lo que significa que tiene mucho de material que se desprende cada vez que se calienta cerca del Sol. Ese material, que permanece aproximadamente a lo largo de la órbita del cometa, cruza la órbita de la Tierra en agosto y crea los meteoros Perseidas que vemos.\n\n"
        + "A continuación se muestran las horas de salida y puesta del sol, así como la puesta y salida de la luna, en el pico de las Perseidas y un día a cada lado de ese pico. En general, las Perseidas están activas desde finales de julio hasta finales de agosto, por lo que es posible que sigas viendo rezagados durante las próximas dos semanas, pero el verdadero espectáculo será del 11 al 13 de agosto, ¡así que asegúrate de no perdértelo!\n\n"
        + "Domingo 11 de agosto\n"
        + "Amanecer: 6:08 a.m.\n"
        + "Atardecer: 8:01 p.m.\n"
        + "Salida de la luna: 1:04 p.m.\n"
        + "Puesta de luna: 23:12\n"
        + "Fase lunar: Luna creciente (41%)\n"
        + "*Las horas de salida, puesta, salida y puesta de la luna se dan en la hora local desde 40° N 90° W. La iluminación de la Luna se da a las 12 p.m. hora local desde el mismo lugar.\n\n"
        + "Lunes 12 de agosto: pico de las Perseidas\n"
        + "Amanecer: 6:09 a.m.\n"
        + "Atardecer: 20:00 h.\n"
        + "Salida de la luna: 2:08 p.m.\n"
        + "Puesta de luna: 23:42\n"
        + "Fase lunar: menguante creciente (51%)\n\n"
        + "Martes 13 de agosto\n"
        + "Amanecer: 6:10 a.m.\n"
        + "Atardecer: 19:59\n"
        + "Salida de la luna: 3:14 p.m.\n"
        + "Puesta de luna: —\n"
        + "Fase lunar: gibosa creciente (61%)",
            12,
            LocalDate.of(2024,8,8)   
        );

        photo = photoToBlob("static/images/meteors.jpeg");
        news5.setPhoto(photo);
        news5.setImage(photo != null);

        newsRepository.save(news1);
        newsRepository.save(news2);
        newsRepository.save(news3);
        newsRepository.save(news4);
        newsRepository.save(news5);

    }


    private void initPictures(){
        Picture picture1 = new Picture(
            "El cazador se levanta",
            "Panagiotis Andreou",
            "Nueva Zelanda",
            LocalDate.of(2024,2,18),
            "Orión el Cazador se eleva sobre Aoraki, también conocido como Monte Cook, la montaña más alta de Nueva Zelanda. El fotógrafo utilizó una cámara DSLR Nikon y un objetivo de 40 mm f/1,4 con ISO 1600 para realizar 12 minutos de exposiciones del cielo, más un fotograma de 90 segundos para la montaña."
        );

        Blob photo = null;
        photo = photoToBlob("static/images/hunter.jpeg");
        picture1.setPhoto(photo);
        picture1.setImage(photo != null);

        Picture picture2 = new Picture(
            "La marca de un lobo",
            "Andrea Arbizzi",
            "Modena, Italia",
            LocalDate.of(2024,4,4),
            "WR 134 en Cygnus es una de las primeras estrellas Wolf-Rayet conocidas, una clase de estrellas raras que llevan el nombre de sus descubridores, los astrónomos franceses Charles Wolf y Georges Rayet. Las estrellas WR originalmente destacaban por sus amplias bandas de emisión. Más tarde, los investigadores descubrieron que eran el resultado de intensos vientos estelares, que pueden formar nebulosas como la que se ve aquí. Esta imagen fue tomada durante 24¾ horas con un filtro de doble banda Ha/OIII y un visor de 8 pulgadas a f/7,3." //
        );

        photo = photoToBlob("static/images/wolf.jpeg");
        picture2.setPhoto(photo);
        picture2.setImage(photo != null);

        Picture picture3 = new Picture(
            "Un diario de viaje de la estrella de la mañana",
            "Tunç Tezel",
            "Bursa, Turquía",
            LocalDate.of(2024,6,4),
            "La aparición matutina de Venus en 2023-2024 se captura en esta secuencia de imágenes, tomadas al mismo tiempo cada mañana despejada desde finales de agosto de 2023 hasta principios de febrero de 2024. Venus ahora se está volviendo más visible en el cielo nocturno, después de haber pasado por una conjunción superior el 4 de junio."
        );

        photo = photoToBlob("static/images/travelog.jpeg");
        picture3.setPhoto(photo);
        picture3.setImage(photo != null);

        Picture picture4 = new Picture(
            "Soplando burbujas",
            "Steve Leonard",
            "Markham, Ontario, Canadá",
            LocalDate.of(2024,1,21),
            "La Nebulosa Burbuja (NGC 7635) es una nebulosa de emisión que rodea a la estrella SAO 20575 en Casiopea; el débil caparazón es creado por sus intensos vientos. El generador de imágenes utilizó un refractor de 4,5 pulgadas f/5,7 para tomar 24 horas de datos SHO y 3 horas de datos RGB. La imagen final es una combinación de una interpretación de la paleta Hubble y una paleta dinámica Foraxx, que utiliza una combinación no lineal de canales."
        );

        photo = photoToBlob("static/images/bubble.jpeg");
        picture4.setPhoto(photo);
        picture4.setImage(photo != null);

        Picture picture5 = new Picture(
            "Centauro A",
            "Vikas Chander",
            "Observatorio El Sauce in the Río Hurtado Valley, Chile",
            LocalDate.of(2023,11,30),
            "A sólo 12 millones de años luz de distancia, Centaurus A (NGC 5128) es la galaxia activa más cercana a nosotros, con un agujero negro supermasivo en su núcleo que escupe material al medio intergaláctico. Es famoso que estos flujos de salida se puedan ver en las emisiones de radio como enormes lóbulos a ambos lados de la galaxia. Sin embargo, esta imagen Hα/OIII/LRGB tomada desde el desierto de Atacama en Chile con un telescopio robótico de 24 pulgadas (y casi 24 horas de exposición) ofrece una vista detallada de las eyecciones de materia en luz visible en la parte inferior derecha."
        );

        photo = photoToBlob("static/images/centaurus.jpeg");
        picture5.setPhoto(photo);
        picture5.setImage(photo != null);

        Picture picture6 = new Picture(
            "Historia de dos senderos",
            "Giovanni Passalacqua",
            "Sicilia, Italia",
            LocalDate.of(2024,6,28),
            "Los rastros dejados por bolas brillantes de gas y trozos de roca fundida se complementan en esta fotografía del Monte Etna en erupción el 28 de junio."
        );

        photo = photoToBlob("static/images/trails.jpeg");
        picture6.setPhoto(photo);
        picture6.setImage(photo != null);

        Picture picture7 = new Picture(
            "Acumulación de tormentas",
            "Mark Johnston",
            "Scottsdale, Arizona",
            LocalDate.of(2024,3,15),
            "Los fuegos artificiales del ciclo solar 25 continúan, como se ve en esta fotografía de Hα del 2 de julio que presenta un filamento en la parte inferior izquierda y protuberancias que saltan del borde del Sol activo. La foto se tomó con un refractor de 6 pulgadas equipado con un filtro de rechazo de energía y un etalón Lunt con un “ocular” Daystar Hα."
        );

        photo = photoToBlob("static/images/storm.jpeg");
        picture7.setPhoto(photo);
        picture7.setImage(photo != null);

        Picture picture8 = new Picture(
            "Estrellas y estrellas de mar",
            "Rob Lyons",
            "Vancouver, Canadá",
            LocalDate.of(2024,5,17),
            "El fotógrafo cuenta cómo consiguió esta espectacular foto:\r\n" + //
            "\r\n" + //
            "He intentado tomar esta fotografía durante tres años durante mis viajes anuales a Tofino, Columbia Británica. El primer año no pude encontrar ninguna estrella de mar. En el segundo año encontré algunas e hice una imagen, pero las estrellas de mar eran tan pequeñas que necesitarías un telescopio para verlas. El año pasado, después de una semana de búsqueda, encontré algunos en mi última noche de viaje. Estas estrellas de mar estaban adheridas a una roca que se encuentra en un lugar popular para practicar surf, por lo que las olas son muy activas. Para obtener el primer plano, fotografié en la hora azul y utilicé mi cámara Sony A7R con una lente de 20 mm f/1,8 para acercarme a la estrella de mar. La marea subía rápido y la estrella de mar ya estaba medio sumergida, así que trabajé rápido para capturar la foto. Luego instalé mi rastreador de estrellas en la seguridad de los acantilados rocosos cercanos, tomé 15 imágenes de la Vía Láctea [con un corte UV/IR de Kolari Vision y un filtro de paso Hα] y las apilé. Luego se combinaron los dos disparos."
        );

        photo = photoToBlob("static/images/starfish.jpeg");
        picture8.setPhoto(photo);
        picture8.setImage(photo != null);

        Picture picture9 = new Picture(
            "Eruipción de auroras",
            "Marty Weintraub",
            "Duluth, Minnesota",
            LocalDate.of(2024,2,6),
            "Las auroras, las nubes y el terreno se combinan para crear la ilusión de un vórtice de luz que brota de las montañas, visto desde Tungeneset en la isla noruega de Senja en esta fotografía tomada el 6 de febrero. El fotógrafo utilizó una cámara sin espejo Sony y una lente de 14 mm a f /1,8 para realizar una exposición de 2,5 segundos a ISO 1600."
        );

        photo = photoToBlob("static/images/aurora.jpg");
        picture9.setPhoto(photo);
        picture9.setImage(photo != null);

        Picture picture10 = new Picture(
            "Un sol tormentoso",
            "Marco Meniero",
            "Roma, Italia",
            LocalDate.of(2023,12,12),
            "La enormidad total del grupo de manchas solares AR 3664 (en la parte inferior del disco solar en esta imagen) es evidente en esta fotografía tomada con una cámara sin espejo Nikon Z9 y una lente de 400 mm a ISO 32. La fotografía es una fotografía compuesta, expuesta al sol. disco (1/32.000 de segundo a f/32) y uno para el paisaje (1/4.000 de segundo y f/9). AR 3664 tiene aproximadamente 16 veces el tamaño de la Tierra y es responsable de la tormenta geomagnética de este fin de semana."
        );

        photo = photoToBlob("static/images/sun.jpeg");
        picture10.setPhoto(photo);
        picture10.setImage(photo != null);

        pictureRepository.save(picture1);
        pictureRepository.save(picture2);
        pictureRepository.save(picture3);
        pictureRepository.save(picture4);
        pictureRepository.save(picture5);
        pictureRepository.save(picture6);
        pictureRepository.save(picture7);
        pictureRepository.save(picture8);
        pictureRepository.save(picture9);
        pictureRepository.save(picture10);
    }

    private void initQuizzes(){
        //Initialize quizz1
        Quizz quizz1 = new Quizz();
        quizz1.setName("Bienvenida");
        quizz1.setDifficulty("Fácil");

        Blob photo = null;
        photo = photoToBlob("static/images/logo2.png");
        quizz1.setPhoto(photo);
        quizz1.setImage(photo != null);

        List<Question> questions = new ArrayList<Question>();
        Question question1 = new Question();
        question1.setQuestion("¿Cómo se llama la página web?");
        question1.setOption1("Astro News");
        question1.setOption2("Cosmic News");
        question1.setOption3("Astronomy For All");
        question1.setOption4("Hidden World");
        question1.setAnswer("Cosmic News");
        question1.setNum(1);
        question1.setQuizz(quizz1);

        question1.setCorrect1(false);
        question1.setCorrect2(true);
        question1.setCorrect3(false);
        question1.setCorrect4(false);

        questions.add(question1);

        Question question2 = new Question();
        question2.setQuestion("¿Si quieres informarte acerca del Sistema Solar dónde debes acudir?");
        question2.setOption1("Sistema Solar");
        question2.setOption2("Perfil");
        question2.setOption3("Fotos");
        question2.setOption4("Calendario");
        question2.setAnswer("Sistema Solar");
        question2.setNum(2);
        question2.setQuizz(quizz1);

        question2.setCorrect1(true);
        question2.setCorrect2(false);
        question2.setCorrect3(false);
        question2.setCorrect4(false);

        questions.add(question2);

        Question question3 = new Question();
        question3.setQuestion("¿Qué puedes encontrar en la aplicación?");
        question3.setOption1("Noticias");
        question3.setOption2("Fotos");
        question3.setOption3("Vídeos");
        question3.setOption4("Todas son correctas");
        question3.setAnswer("Todas son correctas");
        question3.setNum(3);
        question3.setQuizz(quizz1);

        question3.setCorrect1(false);
        question3.setCorrect2(false);
        question3.setCorrect3(false);
        question3.setCorrect4(true);

        questions.add(question3);

        Question question4 = new Question();
        question4.setQuestion("¿Dónde puedes encontrar la fecha de eventos astronómicos?");
        question4.setOption1("Perfil");
        question4.setOption2("Sistema Solar");
        question4.setOption3("Calendario");
        question4.setOption4("Quizzes");
        question4.setAnswer("Calendario");
        question4.setNum(4);
        question4.setQuizz(quizz1);

        question4.setCorrect1(false);
        question4.setCorrect2(false);
        question4.setCorrect3(true);
        question4.setCorrect4(false);

        questions.add(question4);

        Question question5 = new Question();
        question5.setQuestion("¿Puedes dar like a las fotos y noticias?");
        question5.setOption1("Sí");
        question5.setOption2("No");
        question5.setOption3("Sólo fotos");
        question5.setOption4("Sólo noticias");
        question5.setAnswer("Sí");
        question5.setNum(5);
        question5.setQuizz(quizz1);

        question5.setCorrect1(true);
        question5.setCorrect2(false);
        question5.setCorrect3(false);
        question5.setCorrect4(false);

        questions.add(question5);

        quizz1.setQuestions(questions);
        quizzRepository.save(quizz1);
    }

    private void initEvents(){
        Event event1 = new Event(
            LocalDate.of(2024,8,19),
            "bi bi-moon-fill",
            "¡Luna llena! La Luna está en el lado opuesto de la Tierra por lo que el Sol la ilumina por completo. La Luna Llena es a las 18:27 UTC."
        );

        Event event2 = new Event(
            LocalDate.of(2024,8,28),
            "bi bi-diagram-3",
            "¡Conjunción de la Luna y Marte! La Luna pasa a unos 5,1º al norte de Marte a las 00:23 UTC. La Luna tiene una magnitud de -11,4 y Marte una magnitud de 0,8. En este momento la fase lunar es del 32,9%."
        );

        eventRepository.save(event1);
        eventRepository.save(event2);
    }

    private void initVideos(){
        Video video1 = new Video(
            "¿Qué pasa dentro de un agujero negro?",
            "6:34",
            "Los agujeros negros son uno de los fenómenos más enigmáticos del universo: para que use formen, primero una estrella tiene morir. Además, todo lo que entra, jamás logra escapar,  tampoco la luz. Y en su corazón, en la llamada singularidad, el tiempo y el espacio se detienen. ¿Pero qué son exactamente los agujeros negros? ¿Y por qué es importante entender qué es la singularidad, ese punto en el que dejan de tener sentido las leyes de la naturaleza que conocemos?",
            "dMEho2ZcVtE"
        );

        Video video2 = new Video(
            "Toda la evolución de la Tierra en solo 10 minutos",
            "10:41",
            "¿Y si repasáramos toda la evolución de la Tierra en solo 9 minutos? ¿Preparado?",
            "XrA5qcs31iw"
        );

        Video video3 = new Video(
            "NOTICIAS DE MARTE - AGOSTO 2024",
            "29:59",
            "Repasamos las últimas noticias acerca del planeta Marte",
            "7HV-grdOmGo"
        );

        Video video4 = new Video(
            "¿Qué es el Sol y cómo funciona? ¿Es una bola de fuego?",
            "13:07",
            "Qué es realmente el Sol y cómo funciona su interior. Aprovechar el Sol mediante esferas de Dyson. Gracias Giordano Bruno a Kepler, Newton y Einstein entre otros, descubrimos el Universo y que el Sol es una estrella, además de ser un reactor de fusión nuclear que nos da calor en forma de radiación electromagnética.",
            "ePY07OymPkU"
        );

        Video video5 = new Video(
            "¿Qué pasaría si un asteroide impactara la Tierra en 2024?",
            "8:03",
            "Un asteroide viene directo hacia la Tierra. Al principio, los científicos dijeron que pasaría de largo, pero al parecer, han cometido graves errores de cálculo.\r\n" + //
                                "\r\n" + //
                                "¿Podríamos alterar la trayectoria del asteroide?\r\n" + //
                                "¿Y por qué dicen que podría ocurrir antes de 2029?",
            "V5Eh_tZP5yQ"
        );

        Video video6 = new Video(
            "¿Cómo es realmente la Vía Láctea?",
            "16:34",
            "Según la hipótesis científica actualmente dominante, hace unos 13.800 millones de años, un grandioso evento llamado \"Big Bang\" dio origen a nuestro universo. Poco después, ondas gravitatorias de increíble fuerza comprimieron el gas primordial en coágulos gigantes, y las inexorables fuerzas de atracción encendieron un fuego termonuclear en sus profundidades, dando al universo la luz de las primeras estrellas. Las mismas fuerzas unieron las luminarias en cúmulos a gran escala y, luego, en estructuras aún más complejas de dimensiones colosales. Miles de millones de años después, uno de estos se convirtió en la cuna de un fenómeno asombroso: la \"vida\". Y aunque el hombre aún no es capaz de salir de su sistema de origen, ha aprendido mucho sobre su propia Galaxia. Entonces, ¿qué sabemos ya sobre la Vía Láctea?",
            "aqxpcRod_9k"
        );

        videoRepository.save(video1);
        videoRepository.save(video2);
        videoRepository.save(video3);
        videoRepository.save(video4);
        videoRepository.save(video5);
        videoRepository.save(video6);     
    }

    public Blob photoToBlob(String photoStr) {
        ClassPathResource image = new ClassPathResource(photoStr);
        try {
            Blob photoBlob = BlobProxy.generateProxy(image.getInputStream(), image.contentLength());
            return photoBlob;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}

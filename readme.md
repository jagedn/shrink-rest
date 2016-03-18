# shrink-rest
Back-end del acortador de URL ShrinkURL
 
 
 El back-end está destinado a recibir solicitudes de acortar URL devolviendo un JSON con el código asignado. 
 Así mismo por simplificar la solución también será el encargado de redireccionar las URL cortas hacia la correcta.
 
Básicamente Shrink-URL mantiene una tabla donde se asignan códigos cortos (entre 1 y n carácteres) pre-generados a URL
  solicitadas, de tal manera que una misma URL solicitada puede estar asignada a varios códigos. Así mismo la asignación
  se realiza de una forma pseudo-aleatoria para evitar predicciones en la medida de lo posible.
 
Mediante configuración podremos ajustar la caducidad de las asignaciones de tal forma que pasado ese tiempo el código
 volverá a estar disponible para una nueva asignación. Existe la posibilidad de reservar códigos de tal forma que sean
 permanentes (por ejemplo para un modelo premium).

Existe también un módulo encargado de generar estadísticas tanto de generación como de uso de los códigos-urls 
  
## Arquitectura

- Tabla de códigos preasignados con un campo para la URL asignada, un campo para indicar si es permanente y otro para
indicar cúando fue realizada la asignación.

- Servicio de generación de códigos mediante configuración que permite en "caliente" añadir nuevos códigos al sistema .

- Pool de "Provider"s de códigos. El sistema mantiene un pool de providers de tal forma que cada uno mantiene un número
de códigos que se van consumiendo de forma pseudo-aleatoria. Una vez consumidos todos sus códigos el provider se descarta
y se aprovisiona uno nuevo, el cual busca y se reserva una cantidad de códigos disponibles. Si en un momento dado el
 sistema se queda sin códigos podría generar más o esperar a que se liberen por caducidad.
 
- Servicio planificado de "limpieza" de códigos el cual se ejecuta cada cierto tiempo (configurable) y realiza una
desasignación de aquellos códigos que se asignaron antes del tiempo que se configure.

- Servicio de generación de estadísticas que recibe eventos de asignaciones y de solicitudes. Actualmente simplemente
muestra por pantalla la información.

- Controller donde se centraliza la solicitud de asignación mediante un POST, y donde se resuelve la redirección a la URL
 asignada.
 
 
En caso de que la solución necesitara una alta disponilidad se podrían desacoplar sistemas de tal forma que se podría
 realizar la asignación en unos sistemas (protegidos incluso mediante algún sistema de usuario-password, etc) y la resolución
 en otros.
 
## Modelo

|Name|Type|Description|
|---|---|---|
|code|String| codigo corto  |
|url|String(2000)| URL solicitada   |
|consumedAt|Timestamp| instante de la asignacion   |
|permanent|Boolean| asignación permantente  |

## Generador de códigos

El algoritmo de generación se basa en convertir un número a caracteres de un array de [a..z]+[A..Z]+[0..9] (62 caracteres)
 mediante iteraciones sobre el módulo del numero de tal forma que el número se va reduciendo y asignando un caracter en
 cada iteración.
Este algoritmo es usado por el servicio PopulateCodeHashService el cual busca el último id creado en la base de datos y
a partir de él va generando nuevos códigos hasta alcanzar el máximo configurado en la aplicación. De esta forma se puede
dimensionar la base de datos a priori.

## Pool de asignaciones

Mediante el uso del proyecto "pool" de Apache, el sistema mantiene una batería de ShrinkCodeProvider que proporcionan
códigos únicos. ShrinkCodeFactory es el encargado de ir creando nuevos providers a medida que estos van quedandose sin
 códigos asignándoles un "chunk" de estos obtenidos de los que se encuentran libres en la base de datos.

## Consumo de códigos

ShrinkService es el encargado de ir consumiendo los códigos para lo cual obtiene un Provider del pool, le solicita un 
código, lo busca en la base de datos y actualiza el objeto. Con el fín de no bloquear al servidor esta acción se realiza
de forma asíncrona mediante el uso de Promise de Grails.
Así mismo envía eventos de "código consumido" para posibles interesados (estadísticas, ...) mediante el sistema de notificaciones
de Grails.


## Controller

Básicamente el controller realiza dos acciones:

- save (POST), llama al servicio ShrinkService para obtener un código y devolverlo junto con la Shrink URL como JSON

- index (GET), resuelve la Shrink URL redirigiendola a la URL correcta en caso de que exista.

## Configuración

Las siguientes variables configuran el comportamiento de la aplicación:

- cleanerCodeHashService.secondsToRemove, indica en segundos cúal es el tiempo mínimo para considerar una asignación caduca

- populateCodeHashService.maxCodes, permite dimensionar los códigos a generar.

- grails.clientURL redirección a utilizar cuando la solicitud no es correcta. Por defecto apunta a una página de pruebas
contenida en la propia aplicación que sirve para demostrar el uso de la aplicación.

## Ejecución

Al ser una aplicación típica Grails basta seguir las directrices de cualquier aplicación de este tipo para su ejecución.
Así mismo en entorno desarrollo utiliza una base de datos H2 por lo que no es necesario configurar nada al respecto.

Una vez que la aplicación se encuentre en ejecución se podrá probar su funcionamiento mediante un navegador:

http://localhost:8080/static/client.html

donde podremos introducir una URL y obtener su Shrink URl






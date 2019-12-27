package com.example.SpringDemo3;

import com.example.SpringDemo3.model.Comments;
import com.example.SpringDemo3.model.User;
import com.example.SpringDemo3.model.UserComments;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class SpringDemo3Application implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringDemo3Application.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringDemo3Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// 1.- Sample observable list o iterable
		// sampleIterable();

		// 2.- Sample observable flatMap
		// sampleflatMap();

		// 3.- Sample observable flatMap
		// sampleToString();

		// 4.- Sample observable to CollectList
		// sampleCollectList();

		// 5.- Sample observable to CollectList
		// sampleUserCommentsFlatMap();

		// 6.- Sample observable to zipwith
		// sampleUserCommentsZipWith();

		// 7.- Sample observable to zipwith
		// sampleUserCommentsZipWith2();

		// 8.- Sample observable to range
		// sampleUserCommentsZiwithRange();

		// 9.- Sample observable with Interval ( it is delay) zipwith
		// sampleZipWithInterval();

		// 10.- Sample observable with Delay Element ( it is delay) zipwith
		// sampleZipWithDelayElement();

		// 11.- Sample observable with Interval infinite
		// sampleZipWithIntevalInfinite();

		// 12.- Sample observable with Delay Element ( it is delay) Retry
		// sampleZipWithIntevalInfiniteRetry();

		// 13.- Sample observable with Interval infinite of create
		// sampleZipWithIntevalInfiniteCreate();

		// 14.- Sample observable with Interval infinite of create
		 sampleBackPressure();

	}

	/**
	 * 14.- Sample observable with Interval infinite of create
	 */
	private void sampleBackPressure () {

		// Solicita la maxima cantidad de elementos que el productor
		// pueda  servir sin limites
		/* Flux
				.range(0,100)
				.log()
				.subscribe(i -> log.info(i.toString()));
		 */
		Flux
				.range(0,100)
				.log()
				.limitRate(10) // lotes hasta 5 elementos
				.subscribe();

				// 1.- Una forma del BackPresurre es instanciando el Subscriber
				/*
				.subscribe(new Subscriber<Integer>() {

					private Subscription subscription;
					private Integer limit = 10;
					private Integer consum = 0;
					@Override
					public void onSubscribe(Subscription subscription) {
						this.subscription = subscription;
						//subscription.request(Long.MAX_VALUE); // Send max element possible
						subscription.request(limit); // Send max element possible
					}

					@Override
					public void onNext(Integer integer) {
						log.info(integer.toString());
						consum = consum + 1;

						if (consum == limit) {
							consum = 0;
							subscription.request(limit);
						}
					}

					@Override
					public void onError(Throwable throwable) {
						log.error("Error BackPressure");
					}

					@Override
					public void onComplete() {

					}
				});
				 */
	}

	/**
	 * 13.- Sample observable with Interval infinite of create
	 */
	private void sampleZipWithIntevalInfiniteCreate() throws InterruptedException {
		Flux.create(emitter -> {
			Timer timer = new Timer();

			timer.schedule(new TimerTask() {

				private Integer count = 0;
				@Override
				public void run() {
					emitter.next(++count);

					if(count == 10) {
						timer.cancel();
						emitter.complete();
					}

					// Comment line code above if youd don't want error sample
					if ( count == 5) {
						timer.cancel();
						emitter.error( new InterruptedException("Error se llego a 5"));
					}
				}
			}, 1000, 1000);
		})
				//.doOnNext(next -> log.info(next.toString()))
				//.doOnComplete(() -> log.info("Hemos terminado al llegar a 10"))
				.subscribe(next -> log.info(next.toString()),
						error -> log.info(error.getMessage()),
						() -> log.info("Hemos terminado, completado"));

	}

	/**
	 * 12.- Sample observable with Interval infinite (Retry)
	 */
	private void sampleZipWithIntevalInfiniteRetry() throws InterruptedException {

		CountDownLatch latch = new CountDownLatch(1);

		Flux.interval(Duration.ofSeconds(1))
				.doOnTerminate(latch::countDown)
				.flatMap(i -> {
					if (i >= 7) {
						return Flux.error(new InterruptedException("Solo hasta 7"));
					}
					return Flux.just(i);
				})
				.map(i -> "Hi " + i)
				.retry(2)
				.subscribe(s -> log.info(s), e -> log.error(e.getMessage()));

		latch.await();

	}

	/**
	 * 11.- Sample observable with Interval infinite
	 */
	private void sampleZipWithIntevalInfinite() throws InterruptedException {

		CountDownLatch latch = new CountDownLatch(1);

		Flux.interval(Duration.ofSeconds(1))
				.doOnTerminate(latch::countDown)
				.flatMap(i -> {
					if (i >= 7) {
						return Flux.error(new InterruptedException("Solo hasta 7"));
					}
					return Flux.just(i);
				})
				.map(i -> "Hi " + i)
				//.doOnNext(s -> log.info(s))
				.subscribe(s -> log.info(s), e -> log.error(e.getMessage()));

		latch.await();

	}


	/**
	 * 10.- Sample observable with Interval ( it is delay) zipwith
	 */
	private void sampleZipWithDelayElement() {
		Flux<Integer> iRange = Flux.range(1,12)
			.delayElements(Duration.ofSeconds(1))
			.doOnNext(i -> log.info(i.toString()));

		iRange.subscribe();
	}

	/**
	 * 9.- Sample observable with Interval ( it is delay) zipwith
	 */
	private void sampleZipWithInterval() {
		Flux<Integer> iRange = Flux.range(1,12);
		Flux<Long> delay = Flux.interval(Duration.ofSeconds(1));

		iRange
				.zipWith(delay, (ra,re) -> ra)
				.doOnNext(i -> log.info(i.toString()))
				.subscribe();
	}

	/**
	 * 8.- sample observable to range (map, zipwith and range)
	 */
	public void sampleUserCommentsZiwithRange() {
		Flux<Integer> iRange = Flux.range(0,4);
		Flux.just(1,2,3,4)
				.map(i -> (i*2))
				.zipWith(iRange, (uno,dos) -> String.format("Primer flux %d , Segundo flux %d", uno, dos))
				.subscribe(element -> log.info(element));
	}

	/**
	 * 6.- Sample observable to zipwith
	 */
	public void sampleUserCommentsZipWith() {
		// Flujo usuario
		Mono<User> usermono = Mono.fromCallable(()->{return createUser();});

		// Flujo comentario
		Mono<Comments> commentusermono = Mono.fromCallable(() -> {
			Comments comments = new Comments();
			comments.addComentario("Hola Dany The Dog");
			comments.addComentario("Mañana vamos al ring");
			comments.addComentario("Estoy preparando un sparring");
			return comments;
		});

		Mono<UserComments> usercommentzip = usermono
				.zipWith(commentusermono, (user, usercomments) -> new UserComments(user, usercomments));

		usercommentzip.subscribe(uc -> log.info(uc.toString()));
	}

	/**
	 * 7.- Sample observable to zipwith2
	 */
	public void sampleUserCommentsZipWith2() {
		// Flujo usuario
		Mono<User> usermono = Mono.fromCallable(()->{return createUser();});

		// Flujo comentario
		Mono<Comments> commentusermono = Mono.fromCallable(() -> {
			Comments comments = new Comments();
			comments.addComentario("Hola Dany The Dog");
			comments.addComentario("Mañana vamos al ring");
			comments.addComentario("Estoy preparando un sparring");
			return comments;
		});

		Mono<UserComments> usercommentzip = usermono
				.zipWith(commentusermono)
				.map(tupla -> {
					User u = tupla.getT1();
					Comments c = tupla.getT2();
					return new UserComments(u, c);
				});

		usercommentzip.subscribe(uc -> log.info(uc.toString()));
	}

	/**
	 * 5.- Sample merge two flows
	 */
	public void sampleUserCommentsFlatMap() {
		// Flujo usuario
		Mono<User> usermono = Mono.fromCallable(()->{return createUser();});

		// Flujo comentario
		Mono<Comments> commentusermono = Mono.fromCallable(() -> {
			Comments comments = new Comments();
			comments.addComentario("Hola Dany The Dog");
			comments.addComentario("Mañana vamos al ring");
			comments.addComentario("Estoy preparando un sparring");
			return comments;
		});

		usermono.flatMap(u -> commentusermono.map(c -> new UserComments(u,c)))
			.subscribe(uc -> log.info(uc.toString()));
	}

	/**
	 * 4.- Sample observable to CollectList
	 * @throws Exception
	 */
	public void sampleCollectList() throws Exception {
		List<User> usuariosList = new ArrayList<>();
		usuariosList.add(new User("Andres", "Calamaro"));
		usuariosList.add(new User("Pedro","Picapiedra"));
		usuariosList.add(new User("Maria","Lapiedra"));
		usuariosList.add(new User("Diego","Maradona"));
		usuariosList.add(new User("Juan","Palomo"));
		usuariosList.add(new User("Bruce","Lee"));
		usuariosList.add(new User("Bruce","Williams"));

		Flux.fromIterable(usuariosList)
				.collectList()
				.subscribe(lista -> {
					lista.forEach(item -> log.info(item.toString()));
				});
	}

	/**
	 * 3.- Sample observable toString
	 * @throws Exception
	 */
	public void sampleToString() throws Exception {
		List<User> usuariosList = new ArrayList<>();
		usuariosList.add(new User("Andres", "Calamaro"));
		usuariosList.add(new User("Pedro","Picapiedra"));
		usuariosList.add(new User("Maria","Lapiedra"));
		usuariosList.add(new User("Diego","Maradona"));
		usuariosList.add(new User("Juan","Palomo"));
		usuariosList.add(new User("Bruce","Lee"));
		usuariosList.add(new User("Bruce","Williams"));

		// Create observalbe with just, List o iterable
		Flux.fromIterable(usuariosList)
				.map(user -> user.getName().toUpperCase().concat(" ").concat(user.getSurName().toUpperCase()))
				.flatMap(nombre -> {
					// Si el elemento es el mismo por cada elemento que es igual al usuario "bruce"
					// devolvemos un nuebo objeto observable y lo estamos uniendo/fusionando al mismo stream
					// a un unico stream o flujo de salida (convirtiendolo a un objeto usuario)
					if(nombre.contains("bruce".toUpperCase())) {
						return Mono.just(nombre);
					} else {
						return Mono.empty();
					}
				})
				.map(nombre -> { return nombre.toLowerCase(); })
						.subscribe(u -> log.info(u.toString()));
	}

	/**
	 * 2.- Sample observable flatMap
	 * @throws Exception
	 */
	public void sampleflatMap() throws Exception {
		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Calamaro");
		usuariosList.add("Pedro Picapiedra");
		usuariosList.add("Maria Lapiedra");
		usuariosList.add("Diego Maradona");
		usuariosList.add("Juan Palomo");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Williams");

		// Create observalbe with just, List o iterable
		Flux.fromIterable(usuariosList)
				.map(nombre -> User.builder().name(nombre.split(" ")[0]).surName(nombre.split(" ")[1]).build())
				.flatMap(elemento -> {
					// Si el elemento es el mismo por cada elemento que es igual al usuario "bruce"
					// devolvemos un nuebo objeto observable y lo estamos uniendo/fusionando al mismo stream
					// a un unico stream o flujo de salida (convirtiendolo a un objeto usuario)
					if(elemento.getName().equalsIgnoreCase("bruce")) {
						return Mono.just(elemento);
					} else {
						return Mono.empty();
					}
				})
				.doOnNext( usuario -> {
					if (usuario == null){
						throw new RuntimeException("Nombres no pueden ser vacios");
					} else {
						System.out.println(usuario.getName().concat(" ").concat(usuario.getSurName()));
					}
				}).map(usuario -> {
					String nombre = usuario.getName().toLowerCase();
					usuario.setName(nombre);
					return usuario;
				}).subscribe(u -> log.info(u.toString()));
	}

	/**
	 * 1.- Sample observable list o iterable
	 * @throws Exception
	 */
	public void sampleIterable() throws Exception {
		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Calamaro");
		usuariosList.add("Pedro Picapiedra");
		usuariosList.add("Maria Lapiedra");
		usuariosList.add("Diego Maradona");
		usuariosList.add("Juan Palomo");

		// Create observalbe with just
		//Flux<String> nombres = Flux.just("Andres Calamaro","Pedro Picapiedra", "Maria Lapiedra", "Diego Maradona", "Juan Palomo");

		// Create observable with List o iterable
		Flux<String> nombres = Flux.fromIterable(usuariosList);

		// Create observalbe with just, List o iterable
		Flux<User> usuarios = nombres.map(nombre -> User.builder().name(nombre.split(" ")[0]).surName(nombre.split(" ")[1]).build())
				.filter(usuario -> usuario.getName().toLowerCase().equals("diego"))
				.doOnNext( usuario -> {
					if (usuario == null){
						throw new RuntimeException("Nombres no pueden ser vacios");
					} else {
						System.out.println(usuario.getName().concat(" ").concat(usuario.getSurName()));
					}
				}).map(usuario -> {
					String nombre = usuario.getName().toLowerCase();
					usuario.setName(nombre);
					return usuario;
				});

		/*
		nombres.subscribe(e -> Log.info(e.toString()), error -> Log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion del observable con exito");
					}
				});

		 */

		// Testing observables is inmutable
		usuarios.subscribe(e -> log.info(e.toString()), error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion del observable con exito");
					}
				});
	}

	public User createUser() {
		return new User("Dany","The Dog");
	}

}

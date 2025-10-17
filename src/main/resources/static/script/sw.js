const CACHE_NAME = 'jmc-catalogo';

const urlsToCache = [
  '/index.html', 
  '/script/script.js',
  '/images/logo-jmc.png',
  '/images/android-chrome-192x192.png',
  '/images/android-chrome-512x512.png'
];

// Evento para primeira instalação do sw
self.addEventListener('install', event => {
  // Espera até que o cache seja completamente populado
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => {
        console.log('Cache aberto');
        // Adiciona todos os arquivos ao cache
        return cache.addAll(urlsToCache);
      })
  );
});

// Evento de Fetch acionado toda vez que a página faz uma requisição
self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request)
      .then(response => {
        if (response) {
          return response;
        }
        return fetch(event.request);
      }
    )
  );
});

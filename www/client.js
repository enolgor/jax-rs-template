const cookiectl = {
  set: (name, value, expiration) => document.cookie = `${name}=${value || ''}${expiration ? `; expires=${new Date(expiration).toUTCString()}` : ''}; path=/`,
  get: (name) => {
    const nameEQ = `${name}=`;
    const cookie = document.cookie.split(';').map(c => c.trim()).find(c => c.indexOf(nameEQ) > -1);
    if (!cookie) return;
    else return cookie.substring(nameEQ.length, cookie.length);
  },
  del: (name) => {
    document.cookie = `${name}=; Max-Age=-99999999;`;
  },
};

const parseJWS = (token) =>  JSON.parse(atob(token.split('.')[1]));

const Client = class Client {
  constructor(baseurl) {
    this.$baseurl = baseurl;
  }
  authenticate(user, pass) {
    return new Promise((accept, reject) => {
      fetch(`${this.$baseurl}/authorization/token/generate`, {
        method: 'GET',
        headers: {
          Authorization: `Basic ${btoa(`${user}:${pass}`)}`,
        },
      }).then(response => {
        if(response.ok) {
          response.json().then(jsonresp => {
            cookiectl.set('ACCESS_TOKEN', jsonresp.accessToken, parseJWS(jsonresp.accessToken).exp * 1000);
            cookiectl.set('REFRESH_TOKEN', jsonresp.refreshToken, parseJWS(jsonresp.refreshToken).exp * 1000);
            accept();
          });
        } else {
          if(response.status === 401) {
            response.json().then(jsonresp => {
              reject({code: jsonresp.code, message: jsonresp.message});
            });
          } else {
            reject({code: 0, message: `Unknown error: ${response.status}`});
          }
        }
      })
      .catch(reject);
    });
  }
  refreshAccessToken() {
    return new Promise((accept, reject) => {
      const refreshToken = cookiectl.get('REFRESH_TOKEN');
      if(!refreshToken) {
        reject({code: Client.authErrorCodes().NOREFRESHTOKEN, message: 'Refresh Token Not Found'});
        return;
      }
      fetch(`${this.$baseurl}/authorization/token/refresh`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${refreshToken}`,
        },
      }).then(response => {
        if(response.ok) {
          response.text().then(accessToken => {
            cookiectl.set('ACCESS_TOKEN', accessToken, parseJWS(accessToken).exp * 1000);
            accept();
          });
        } else {
          if(response.status === 401) {
            response.json().then(jsonresp => {
              reject({code: jsonresp.code, message: jsonresp.message});
            });
          } else {
            reject({code: authErrorCodes().OTHER, message: `Unknown error: ${response.status}`});
          }
        }
      })
      .catch(reject);
    });
  }
  request(path, options) {
    return new Promise((accept, reject) => {
      const accessToken = cookiectl.get('ACCESS_TOKEN');
      if(!accessToken) {
        this.refreshAccessToken().then(() => this.request(path, options)).then(accept).catch(reject);
        return;
      }
      options = options || {};
      options.headers = options.headers || {};
      options.headers.Authorization =  `Bearer ${accessToken}`;
      fetch(`${this.$baseurl}${path}`, options).then(response => {
        if(response.ok) {
          response.text().then(accept).catch(reject);
        } else {
          response.text().then(reject).catch(reject);
        }
      });
    });
  }
  static authErrorCodes() {
    return {
      //LOCAL
      NOREFRESHTOKEN: -1,
      //UNKNOWN
      OTHER: 0,
      //From Server
      UNDEFINED: 1,
      UNSUPPORTED: 2,
      MALFORMED: 3,
      INCORRECTUSERPASS: 4,
      TOKENNOTFOUND: 5,
      TOKENEXPIRED: 6,
      UNTRUSTEDTOKEN: 7,
      INCORRECTTOKEN: 8,
    };
  }
  logout() {
    const refreshToken = cookiectl.get('REFRESH_TOKEN');
    return new Promise((accept, reject) => {
      if(!refreshToken) {
        accept();
        return;
      }
      this.get(`/authorization/token/revoke/${refreshToken}`).then(() => {
        cookiectl.del('REFRESH_TOKEN');
        cookiectl.del('ACCESS_TOKEN');
        accept();
      }).catch(reject);
    });
  }
  closeAllSessions() {
    this.get('/authorization/token/revokeAll').then(() => {
      cookiectl.del('REFRESH_TOKEN');
      cookiectl.del('ACCESS_TOKEN');
      accept();
    }).catch(reject);
  }
  get(path) {
    return this.request(path, { method: 'GET' });
  }
  post(path, body) {
    return this.request(path, { method: 'POST', body });
  }
  put(path, body) {
    return this.request(path, { method: 'PUT', body });
  }
  delete(path) {
    return this.request(path, { method: 'DELETE' });
  }
  get isAuthenticated() {
    const refreshToken = cookiectl.get('REFRESH_TOKEN');
    return !!refreshToken;
    
  }
  get username() {
    const refreshToken = cookiectl.get('REFRESH_TOKEN');
    return refreshToken ?  parseJWS(refreshToken).sub : null;
  }
}
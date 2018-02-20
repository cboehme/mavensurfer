import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

class JobSubmission {

  settings: { [key:string]:string };

  constructor(public type: string) {}

}

@Injectable()
export class SubmissionService {

  private baseUrl = 'http://localhost:8080/api/indexing-jobs';

  constructor(private http: HttpClient) { }

  submitNexusJob(nexusUrl: string, repositoryId: string, basePath: string) {
    const jobSubmission = new JobSubmission('nexus-indexing-job');
    jobSubmission.settings = {
      'nexus-url': nexusUrl,
      'repository-id': repositoryId,
      'base-path': basePath
    };
    return this.http.post(this.baseUrl, jobSubmission);
  }

}
